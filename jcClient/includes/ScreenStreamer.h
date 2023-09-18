#ifndef CLIENT_SCREENSTREAMER_H
#define CLIENT_SCREENSTREAMER_H

#include "Stream.h"
#include "KeyboardExecuter.h"
#include "Sender.h"
#include "Gdiplus.h"
#include "ClientSocket.h"
#include "Handler.h"
#include "BlockingQueue.h"


class ScreenStreamer : public Handler {

public:
    explicit ScreenStreamer(ClientSocket &clientSocket);
    void startStreaming(nlohmann::json jsonObjet);
private:
    static BITMAPINFOHEADER createBitmapHeader(int,int);
    std::string clickKeyWord = "click/";
    std::string keyKeyWord = "key/";
    std::atomic<bool> streamingState = false;
    void screenTransmissionThread();
    static void clickOnCoordinates(std::vector<int> infoOfClick);
    void screenEventsThread();
    void stopStreaming();
    byte channelID;
    BlockingQueue<std::string> blockingQueue;
    static void takeScreenshot(std::vector<BYTE> &data);
    void addKeyToQueue(nlohmann::json jsonObject);
    static HBITMAP GdiPlusScreenCapture(HWND hWnd);

    static bool saveToMemory(HBITMAP *hbitmap, std::vector<BYTE> &data);
};


#endif
