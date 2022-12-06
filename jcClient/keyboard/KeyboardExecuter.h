#ifndef CLIENT_KEYBOARDEXECUTER_H
#define CLIENT_KEYBOARDEXECUTER_H
#include <string>
#include <windows.h>
#include <sstream>
#include <iostream>
#include <regex>
#include <thread>
#include "../stream/Stream.h"

class KeyboardExecuter {
public:
    explicit KeyboardExecuter(const Stream &);
    void executeSequence();
    static void pressKey(UCHAR virtualKey);
    void execute();

private:
    Stream stream;
    static std::vector<std::string> getVectorDividedByRegex(const std::string &,const std::regex&);
    std::string sequence;
};


#endif
