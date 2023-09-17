#ifndef CLIENT_SCREENSTREAMER_H
#define CLIENT_SCREENSTREAMER_H

#include "Stream.h"
#include "KeyboardExecuter.h"
#include "Sender.h"
#include "Gdiplus.h"
#include "ClientSocket.h"
#include "Handler.h"



class ScreenStreamer : public Handler {

public:
    explicit ScreenStreamer(ClientSocket &clientSocket);
    void startStreaming(nlohmann::json jsonObjet);
private:
    static BITMAPINFOHEADER createBitmapHeader(int,int);
    std::string clickKeyWord = "click/";
    std::string keyKeyWord = "key/";
    void screenTransmissionThread();
    void screenEventsThread();
    byte channelID;
    static void takeScreenshot(std::vector<BYTE> &data);

    static HBITMAP GdiPlusScreenCapture(HWND hWnd);

    static bool saveToMemory(HBITMAP *hbitmap, std::vector<BYTE> &data);
};


#endif
