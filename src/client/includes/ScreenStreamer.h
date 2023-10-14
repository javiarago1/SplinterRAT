#ifndef CLIENT_SCREENSTREAMER_H
#define CLIENT_SCREENSTREAMER_H


#include "KeyboardExecuter.h"

#include "Gdiplus.h"
#include "ClientSocket.h"
#include "Handler.h"
#include "BlockingQueue.h"

struct MonitorInfo {
    RECT rect;
    std::string deviceName;
};


class ScreenStreamer : public Handler {

public:
    explicit ScreenStreamer(ClientSocket &clientSocket);
    void startStreaming(nlohmann::json jsonObjet);
    static std::map<std::string, RECT> monitorMap;
    ~ScreenStreamer();
private:
    static RECT selectedMonitor;
    static BOOL CALLBACK MonitorEnumProc(HMONITOR, HDC, LPRECT, LPARAM);
    static BITMAPINFOHEADER createBitmapHeader(int,int);
    std::string clickKeyWord = "click/";
    std::string keyKeyWord = "key/";
    std::atomic<bool> streamingState = false;
    void screenTransmissionThread();
    static void clickOnCoordinates(const nlohmann::json& clickData);
    void screenEventsThread();
    void stopStreaming();
    byte channelID;
    BlockingQueue<std::string> blockingQueue;
    static void takeScreenshot(std::vector<BYTE> &data);
    void addKeyToQueue(nlohmann::json jsonObject);
    static HBITMAP GdiPlusScreenCapture(HWND hWnd, RECT targetMonitorRect);
    void sendMonitors();
    static bool saveToMemory(HBITMAP *hbitmap, std::vector<BYTE> &data);
    ULONG_PTR gdiplusToken;
};


#endif
