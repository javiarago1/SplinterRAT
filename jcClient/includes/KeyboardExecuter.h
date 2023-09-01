#ifndef CLIENT_KEYBOARDEXECUTER_H
#define CLIENT_KEYBOARDEXECUTER_H
#include <string>
#include "Stream.h"
#include <windows.h>
#include <sstream>
#include <iostream>
#include <regex>
#include <thread>
#include "json.hpp"
#include "Sender.h"


class KeyboardExecuter : public Sender {
public:
    KeyboardExecuter(const Stream &stream, std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap);
    void executeSequence(const std::string&);
    static void pressKey(UCHAR virtualKey);
    void executeCommand(nlohmann::json);

    void send() override;

private:
    static std::vector<std::string> getVectorDividedByRegex(const std::string &,const std::regex&);
    std::string sequence;
};


#endif
