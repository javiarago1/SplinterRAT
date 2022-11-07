#ifndef CLIENT_SCREENSTREAMER_H
#define CLIENT_SCREENSTREAMER_H


#include <windows.h>
#include <opencv2/opencv.hpp>
#include "../stream/Stream.h"

class ScreenStreamer {

public:
    ScreenStreamer(Stream);
    void sendPicture();
private:
    Stream stream;
    BITMAPINFOHEADER createBitmapHeader(int,int);
    cv::Mat captureScreenMat(HWND hwnd);

};


#endif
