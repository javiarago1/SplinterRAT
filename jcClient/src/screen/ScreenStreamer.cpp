#include "ScreenStreamer.h"

ScreenStreamer::ScreenStreamer(ClientSocket &clientSocket) :
        Handler(clientSocket) {
    ActionMap actionMap = clientSocket.getActionMap();
    actionMap["START_SCREEN_STREAMING"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &ScreenStreamer::startStreaming,json);
        threadGen.runInNewThread(this, &ScreenStreamer::screenEventsThread);
    };
    actionMap["STOP_SCREEN_STREAMING"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &ScreenStreamer::stopStreaming);
    };
    actionMap["KEY_EXECUTION"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &ScreenStreamer::addKeyToQueue, json);
    };

}

void ScreenStreamer::stopStreaming(){
    streamingState.store(false);
    blockingQueue.push("END");
}

void ScreenStreamer::clickOnCoordinates(std::vector<int> infoOfClick) {
    double fScreenWidth = ::GetSystemMetrics(SM_CXSCREEN) - 1;
    double fScreenHeight = ::GetSystemMetrics(SM_CYSCREEN) - 1;
    double fx = infoOfClick[2] * (65535.0f / fScreenWidth);
    double fy = infoOfClick[3] * (65535.0f / fScreenHeight);
    std::cout << fScreenWidth << std::endl;
    std::cout << fScreenHeight << std::endl;
    INPUT Input = {0};
    Input.type = INPUT_MOUSE;
    Input.mi.dwFlags = MOUSEEVENTF_MOVE | MOUSEEVENTF_ABSOLUTE;
    Input.mi.dx = (LONG) fx;
    Input.mi.dy = (LONG) fy;
    SendInput(1, &Input, sizeof(INPUT));
    ::ZeroMemory(&Input, sizeof(INPUT));
    Input.type = INPUT_MOUSE;
    Input.mi.dwFlags = infoOfClick[0] | infoOfClick[1];
    ::SendInput(1, &Input, sizeof(INPUT));
    ::ZeroMemory(&Input, sizeof(INPUT));
}

void ScreenStreamer::screenEventsThread() {
    std::string whereTo;
    while ((whereTo = blockingQueue.pop()) != "END") {
        if (whereTo.rfind(clickKeyWord) != -1) {
            std::string newString = whereTo.substr(clickKeyWord.length(), whereTo.length());
            std::string segment;
            std::vector<int> seglist;
            std::stringstream ss(newString);

            while (std::getline(ss, segment, ',')) {
                std::cout << newString << std::endl;
                seglist.push_back(stoi(segment));
            }

            clickOnCoordinates(seglist);
        } else if (whereTo.rfind(keyKeyWord, 0) == 0) {
            char character = whereTo.at(keyKeyWord.length());
            KeyboardExecuter::pressKey(VkKeyScanA(character));
        }
    }
}

const int FRAGMENT_SIZE = 204800; // 200 KB
const uint8_t LAST_FRAGMENT = 0x02;
const uint8_t NOT_LAST_FRAGMENT = 0x01;

void ScreenStreamer::screenTransmissionThread() {
    streamingState.store(true);
    while (streamingState.load()) {
        std::vector<BYTE> screenBuff;
        takeScreenshot(screenBuff);

        std::vector<uint8_t> buffer(FRAGMENT_SIZE);
        size_t bytesRead = 0;
        size_t totalSize = screenBuff.size();
        size_t offset = 0;

        while (totalSize > 0) {
            // Preparar los bytes de encabezado: 1 byte para el ID de archivo, 1 byte para el código de control
            buffer[0] = channelID;
            buffer[1] = NOT_LAST_FRAGMENT;

            // Calcular cuántos bytes leer para este fragmento
            if (totalSize >= FRAGMENT_SIZE - 2) {
                bytesRead = FRAGMENT_SIZE - 2;
            } else {
                bytesRead = totalSize;
                buffer[1] = LAST_FRAGMENT;
            }

            // Copiar los datos en el buffer, comenzando en el desplazamiento 2
            std::copy(screenBuff.begin() + offset, screenBuff.begin() + offset + bytesRead, buffer.begin() + 2);

            // Enviar el fragmento
            clientSocket.sendBytes(std::vector<uint8_t>(buffer.begin(), buffer.begin() + bytesRead + 2));

            // Actualizar el desplazamiento y el tamaño total restante
            offset += bytesRead;
            totalSize -= bytesRead;
        }

        // Limpiar el buffer
        screenBuff.clear();
    }
}





void ScreenStreamer::addKeyToQueue(nlohmann::json jsonObject){
    blockingQueue.push(jsonObject["key"]);
}

void ScreenStreamer::startStreaming(nlohmann::json jsonObjet) {

    int fScreenWidth = ::GetSystemMetrics(SM_CXSCREEN);
    int fScreenHeight = ::GetSystemMetrics(SM_CYSCREEN);

    std::vector<int> dimensions = {fScreenWidth, fScreenHeight};
    nlohmann::json json;
    json["RESPONSE"] = "SCREEN_DIMENSIONS";
    json["dimensions"] = dimensions;
    channelID = jsonObjet["channel_id"];
    clientSocket.sendMessage(json.dump());

    std::thread transmissionThread(&ScreenStreamer::screenTransmissionThread, this);
    std::thread eventsThread(&ScreenStreamer::screenEventsThread, this);

    transmissionThread.join();
    eventsThread.join();

}

// -------------------------------------

BITMAPINFOHEADER ScreenStreamer::createBitmapHeader(int width, int height)
{
    BITMAPINFOHEADER  bi;

    // create a bitmap
    bi.biSize = sizeof(BITMAPINFOHEADER);
    bi.biWidth = width;
    bi.biHeight = -height;  //this is the line that makes it draw upside down or not
    bi.biPlanes = 1;
    bi.biBitCount = 32;
    bi.biCompression = BI_RGB;
    bi.biSizeImage = 0;
    bi.biXPelsPerMeter = 0;
    bi.biYPelsPerMeter = 0;
    bi.biClrUsed = 0;
    bi.biClrImportant = 0;

    return bi;
}

HBITMAP ScreenStreamer::GdiPlusScreenCapture(HWND hWnd)
{
    // get handles to a device context (DC)
    HDC hwindowDC = GetDC(hWnd);
    HDC hwindowCompatibleDC = CreateCompatibleDC(hwindowDC);
    SetStretchBltMode(hwindowCompatibleDC, COLORONCOLOR);

    // define scale, height and width
    int screenx = GetSystemMetrics(SM_XVIRTUALSCREEN);
    int screeny = GetSystemMetrics(SM_YVIRTUALSCREEN);
    int width = GetSystemMetrics(SM_CXVIRTUALSCREEN);
    int height = GetSystemMetrics(SM_CYVIRTUALSCREEN);

    // create a bitmap
    HBITMAP hbwindow = CreateCompatibleBitmap(hwindowDC, width, height);
    BITMAPINFOHEADER bi = createBitmapHeader(width, height);

    // use the previously created device context with the bitmap
    SelectObject(hwindowCompatibleDC, hbwindow);

    // Starting with 32-bit Windows, GlobalAlloc and LocalAlloc are implemented as wrapper functions that call HeapAlloc using a handle to the process's default heap.
    // Therefore, GlobalAlloc and LocalAlloc have greater overhead than HeapAlloc.
    DWORD dwBmpSize = ((width * bi.biBitCount + 31) / 32) * 4 * height;
    HANDLE hDIB = GlobalAlloc(GHND, dwBmpSize);
    char* lpbitmap = (char*)GlobalLock(hDIB);

    // copy from the window device context to the bitmap device context
    StretchBlt(hwindowCompatibleDC, 0, 0, width, height, hwindowDC, screenx, screeny, width, height, SRCCOPY);   //change SRCCOPY to NOTSRCCOPY for wacky colors !
    GetDIBits(hwindowCompatibleDC, hbwindow, 0, height, lpbitmap, (BITMAPINFO*)&bi, DIB_RGB_COLORS);

    // avoid memory leak
    GlobalUnlock(hDIB);
    GlobalFree(hDIB);
    DeleteDC(hwindowCompatibleDC);
    ReleaseDC(hWnd, hwindowDC);

    return hbwindow;
}

bool ScreenStreamer::saveToMemory(HBITMAP* hbitmap, std::vector<BYTE>& data)
{
    Gdiplus::Bitmap bmp(*hbitmap, nullptr);
    // write to IStream
    IStream* istream = nullptr;
    CreateStreamOnHGlobal(nullptr, TRUE, &istream);

    // define encoding
    CLSID clsid;
    CLSIDFromString(L"{557cf406-1a04-11d3-9a73-0000f81ef32e}", &clsid);

    Gdiplus::Status status = bmp.Save(istream, &clsid, nullptr);
    if (status != Gdiplus::Status::Ok)
        return false;

    // get memory handle associated with istream
    HGLOBAL hg = nullptr;
    GetHGlobalFromStream(istream, &hg);

    // copy IStream to buffer
    SIZE_T bufsize = GlobalSize(hg);
    data.resize(bufsize);

    // lock & unlock memory
    LPVOID pimage = GlobalLock(hg);
    memcpy(&data[0], pimage, bufsize);
    GlobalUnlock(hg);
    istream->Release();
    return true;
}

void ScreenStreamer::takeScreenshot(std::vector<BYTE> &data){
    Gdiplus::GdiplusStartupInput gdiplusStartupInput;
    ULONG_PTR gdiplusToken;
    GdiplusStartup(&gdiplusToken, &gdiplusStartupInput, nullptr);
    HWND hWnd = GetDesktopWindow();
    HBITMAP hBmp = GdiPlusScreenCapture(hWnd);
    // save as png to memory
    saveToMemory(&hBmp, data);

    DeleteObject(hBmp); // Nueva línea para liberar el bitmap
    Gdiplus::GdiplusShutdown(gdiplusToken); // Nueva línea para liberar recursos de GDI+
    CoUninitialize(); // Nueva línea para liberar recursos de COM

}
