#ifndef CLIENT_SCREENSTREAMER_H
#define CLIENT_SCREENSTREAMER_H

#include "Stream.h"
#include <opencv2/opencv.hpp>
#include "KeyboardExecuter.h"
#include "Sender.h"
#include "Gdiplus.h"



class ScreenStreamer : public Sender {

public:
    explicit ScreenStreamer(const Stream &stream, const Stream &auxEventStream);
    void send() override;
    void startStreaming(nlohmann::json jsonObjet);
private:
    static BITMAPINFOHEADER createBitmapHeader(int,int);
    static cv::Mat captureScreenMat(HWND hwnd);
    std::string clickKeyWord = "click/";
    std::string keyKeyWord = "key/";
    Stream auxEventStream;
    void screenTransmissionThread();
    void screenEventsThread();

    void takeScreenshot(std::vector<BYTE> &data);

    HBITMAP GdiPlusScreenCapture(HWND hWnd);

    bool saveToMemory(HBITMAP *hbitmap, std::vector<BYTE> &data, std::string dataFormat);
};


#endif
