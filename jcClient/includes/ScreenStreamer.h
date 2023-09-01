#ifndef CLIENT_SCREENSTREAMER_H
#define CLIENT_SCREENSTREAMER_H

#include "Stream.h"
#include "KeyboardExecuter.h"
#include "Sender.h"
#include "Gdiplus.h"



class ScreenStreamer : public Sender {

public:
    explicit ScreenStreamer(const Stream &stream, const Stream &auxEventStream, std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap);
    void send() override;
    void startStreaming(nlohmann::json jsonObjet);
private:
    static BITMAPINFOHEADER createBitmapHeader(int,int);
    std::string clickKeyWord = "click/";
    std::string keyKeyWord = "key/";
    Stream auxEventStream;
    void screenTransmissionThread();
    void screenEventsThread();

    static void takeScreenshot(std::vector<BYTE> &data);

    static HBITMAP GdiPlusScreenCapture(HWND hWnd);

    static bool saveToMemory(HBITMAP *hbitmap, std::vector<BYTE> &data);
};


#endif
