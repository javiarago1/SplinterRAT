#include <winsock2.h>
#include <string>
#include <thread>
#include <opencv2/opencv.hpp>
#include "../stream/Stream.h"
#include "../time/Time.h"

#ifndef CLIENT_WEBCAMMANAGER_H
#define CLIENT_WEBCAMMANAGER_H


class WebcamManager {
private:
    // information related to recording
    Stream stream;
    int webcamID;
    bool fragmented;
    int FPS;
    // video and file managing
    cv::VideoWriter output;
    std::vector<std::wstring> pathVector;
    std::wstring fileName;
    boolean initialized = false;

    void sendRecord();
    void removeTempFiles();
    void sendFrame(const cv::Mat&);
    void sendDimensions(int,int);

public:
    WebcamManager(Stream stream, int webcamID, bool fragmented, int FPS);
    void startWebcam();


};
#endif