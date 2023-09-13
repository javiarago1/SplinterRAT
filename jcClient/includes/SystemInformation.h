#ifndef CLIENT_CLIENTINFORMATION_H
#define CLIENT_CLIENTINFORMATION_H
#include "Stream.h"
#include <windows.h>
#include <string>
#include <lmcons.h>
#include <sys/stat.h>
#include <vector>
#include <iterator>
#include <sstream>
#include "configuration.h"
#include "Sender.h"
#include "FileManager.h"

class SystemInformation {
public:
    static std::string getWindowsVersion();

    static std::string getUsername();

    static nlohmann::json getSystemInformation();


};


#endif
