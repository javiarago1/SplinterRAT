#include <windows.h>
#include <string>
#include <lmcons.h>
#include <sys/stat.h>
#include <vector>
#include <iterator>
#include <sstream>

#ifndef CLIENT_CLIENTINFORMATION_H
#define CLIENT_CLIENTINFORMATION_H


class SystemInformation {

public:
    SystemInformation();

    static std::string getWindowsVersion();

    static std::vector<std::string> getDisks();

    static std::string getUsername();

    static std::vector<std::string> getSystemInformation();

    static std::string vector2string (std::vector<std::string>);
};


#endif //CLIENT_CLIENTINFORMATION_H
