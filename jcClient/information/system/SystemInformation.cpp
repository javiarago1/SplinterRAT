#include "SystemInformation.h"

std::string SystemInformation::getWindowsVersion() {
    OSVERSIONINFOEX info;
    ZeroMemory(&info, sizeof(OSVERSIONINFOEX));
    info.dwOSVersionInfoSize = sizeof(OSVERSIONINFOEX);
    GetVersionEx((LPOSVERSIONINFO) &info);
    std::string str = "Windows ";
    str.append(std::to_string(info.dwMajorVersion));
    str.append(".");
    str.append(std::to_string(info.dwMinorVersion));
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
            printf("Drive: %s\n", szSingleDrive);
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

std::string SystemInformation::vector2string(std::vector<std::string> vector){
    std::ostringstream vts;
    std::copy(vector.begin(), vector.end()-1,
              std::ostream_iterator<std::string>(vts, ", "));

    // Now add the last element with no delimiter
    vts << vector.back();
    return vts.str();
}

std::vector<std::string> SystemInformation::getSystemInformation() {
    std::vector<std::string> informationVector;
    informationVector.push_back(getWindowsVersion());
    informationVector.emplace_back(getenv("USERPROFILE"));
    informationVector.emplace_back(getenv("HOMEPATH"));
    informationVector.emplace_back(getenv("HOMEDRIVE"));
    informationVector.push_back(getUsername());
    informationVector.push_back(vector2string(getDisks()));

    return informationVector;
}

SystemInformation::SystemInformation() = default;
