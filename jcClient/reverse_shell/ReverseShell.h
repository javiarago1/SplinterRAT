#ifndef CLIENT_REVERSESHELL_H
#define CLIENT_REVERSESHELL_H

#include <string>


class ReverseShell {
public:
    ReverseShell() = default;
    static std::string executeCommand(const std::wstring&);
};


#endif
