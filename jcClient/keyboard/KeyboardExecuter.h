#ifndef CLIENT_KEYBOARDEXECUTER_H
#define CLIENT_KEYBOARDEXECUTER_H
#include <string>
#include <windows.h>
#include <sstream>
#include <iostream>
#include <regex>

class KeyboardExecuter {
public:
    KeyboardExecuter(const std::string & sequence);
    void executeSequence();
    void pressKey(UCHAR virtualKey);

private:
    std::vector<std::string> getVectorDividedByRegex(const std::string &,const std::regex&);
    std::string sequence;
    INPUT ip;



};


#endif
