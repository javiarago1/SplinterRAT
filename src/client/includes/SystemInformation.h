#ifndef CLIENT_CLIENTINFORMATION_H
#define CLIENT_CLIENTINFORMATION_H

#include <windows.h>
#include <string>
#include <lmcons.h>
#include <sys/stat.h>
#include <vector>
#include <iterator>
#include <sstream>
#include "configuration.h"

#include "FileManager.h"

class SystemInformation {
public:
    static std::string getWindowsVersion();

    static std::string getUsername();

    static nlohmann::json getSystemInformation();

    static std::string getPrimaryMACAddress();

};


#endif
