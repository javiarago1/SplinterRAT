#ifndef CLIENT_SCREENSTREAMER_H
#define CLIENT_SCREENSTREAMER_H

#include "Stream.h"
#include <opencv2/opencv.hpp>
#include "KeyboardExecuter.h"
#include "Sender.h"


class ScreenStreamer : public Sender {

public:
    explicit ScreenStreamer(const Stream&);
    void send() override;
private:
    static BITMAPINFOHEADER createBitmapHeader(int,int);
    static cv::Mat captureScreenMat(HWND hwnd);

};


#endif
