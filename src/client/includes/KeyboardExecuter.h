#ifndef CLIENT_KEYBOARDEXECUTER_H
#define CLIENT_KEYBOARDEXECUTER_H
#include <string>

#include <windows.h>
#include <sstream>
#include <iostream>
#include <regex>
#include <thread>
#include "json.hpp"

#include "Handler.h"


class KeyboardExecuter : public Handler {
public:
    explicit KeyboardExecuter(ClientSocket &clientSocket);
    void executeSequence(const std::string&);
    static void pressKey(UCHAR virtualKey);
    void executeCommand(nlohmann::json);

private:
    static std::vector<std::string> getVectorDividedByRegex(const std::string &,const std::regex&);
    std::string sequence;
};


#endif
