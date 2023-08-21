#include "ScreenStreamer.h"

BITMAPINFOHEADER ScreenStreamer::createBitmapHeader(int width, int height) {
    BITMAPINFOHEADER bi;

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


cv::Mat ScreenStreamer::captureScreenMat(HWND hwnd) {
    cv::Mat src;
    // get handles to a device context (DC)
    HDC hwindowDC = GetDC(hwnd);
    HDC hwindowCompatibleDC = CreateCompatibleDC(hwindowDC);
    SetStretchBltMode(hwindowCompatibleDC, COLORONCOLOR);

    // define scale, height and width
    int screenx = GetSystemMetrics(SM_XVIRTUALSCREEN);
    int screeny = GetSystemMetrics(SM_YVIRTUALSCREEN);
    int width = GetSystemMetrics(SM_CXVIRTUALSCREEN);
    int height = GetSystemMetrics(SM_CYVIRTUALSCREEN);

    // create mat object
    src.create(height, width, CV_8UC4);

    // create a bitmap
    HBITMAP hbwindow = CreateCompatibleBitmap(hwindowDC, width, height);
    BITMAPINFOHEADER bi = createBitmapHeader(width, height);

    // use the previously created device context with the bitmap
    SelectObject(hwindowCompatibleDC, hbwindow);

    // copy from the window device context to the bitmap device context
    StretchBlt(hwindowCompatibleDC, 0, 0, width, height, hwindowDC, screenx, screeny, width, height,
               SRCCOPY);  //change SRCCOPY to NOTSRCCOPY for wacky colors !
    GetDIBits(hwindowCompatibleDC, hbwindow, 0, height, src.data, (BITMAPINFO *) &bi,
              DIB_RGB_COLORS);            //copy from hwindowCompatibleDC to hbwindow

    // avoid memory leak
    DeleteObject(hbwindow);
    DeleteDC(hwindowCompatibleDC);
    ReleaseDC(hwnd, hwindowDC);

    return src;
}

void clickOnCoordinates(std::vector<int> infoOfClick){
    double fScreenWidth = ::GetSystemMetrics(SM_CXSCREEN) - 1;
    double fScreenHeight = ::GetSystemMetrics(SM_CYSCREEN) - 1;
    double fx = infoOfClick[2]*(65535.0f / fScreenWidth);
    double fy = infoOfClick[3]*(65535.0f / fScreenHeight);
    std::cout << fScreenWidth << std::endl;
    std::cout << fScreenHeight << std::endl;
    INPUT  Input = { 0 };
    Input.type = INPUT_MOUSE;
    Input.mi.dwFlags = MOUSEEVENTF_MOVE | MOUSEEVENTF_ABSOLUTE;
    Input.mi.dx = (LONG)fx;
    Input.mi.dy = (LONG)fy;
    SendInput(1, &Input, sizeof(INPUT));
    ::ZeroMemory(&Input,sizeof(INPUT));
    Input.type      = INPUT_MOUSE;
    Input.mi.dwFlags  = infoOfClick[0] | infoOfClick[1];
    ::SendInput(1,&Input,sizeof(INPUT));
    ::ZeroMemory(&Input,sizeof(INPUT));
}

void ScreenStreamer::send() {
    double fScreenWidth = ::GetSystemMetrics(SM_CXSCREEN) - 1;
    double fScreenHeight = ::GetSystemMetrics(SM_CYSCREEN) - 1;
    std::string screenDimensions(std::to_string((int)fScreenWidth)+","+std::to_string((int)fScreenHeight));
    stream.sendString(screenDimensions.c_str());
    std::string clickKeyWord("click/");
    std::string keyKeyWord("key/");

    std::string whereTo;
    while ((whereTo= stream.readString())!="END") {
        if (whereTo.rfind(clickKeyWord)!=-1){
            std::string newString = whereTo.substr(clickKeyWord.length(),whereTo.length());
            std::string segment;
            std::vector<int> seglist;
            std::stringstream ss(newString);

            while(std::getline(ss, segment, ','))
            {
                std::cout << newString << std::endl;
                seglist.push_back(stoi(segment));
            }

            clickOnCoordinates(seglist);
        } else if (whereTo.rfind(keyKeyWord,0)==0){
            char character = whereTo.at(keyKeyWord.length());
            KeyboardExecuter::pressKey(VkKeyScanA(character));
        }
        // capture image
        HWND hwnd = GetDesktopWindow();
        cv::Mat src = captureScreenMat(hwnd);

        // encode result
        std::vector<uchar> buff;
        cv::imencode(".png", src, buff);
        stream.sendSize((int) buff.size());
        ::send(stream.getSock(), (char *) &buff[0], (int) buff.size(), 0);
        // save img
        buff.clear();
    }
}



ScreenStreamer::ScreenStreamer(const Stream& stream) : Sender(stream){

}
