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
    void executeSequence(const nlohmann::json & sequenceJson);
    static void pressKey(UCHAR virtualKey);
    void executeCommand(nlohmann::json);

private:
    std::string sequence;
};


#endif
