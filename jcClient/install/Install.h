#ifndef CLIENT_INSTALL_H
#define CLIENT_INSTALL_H
#include <string>
#include <filesystem>
#include <iostream>
#include "../configuration.h"
#include <windows.h>
#include <shlobj.h>
#include <strsafe.h>


class Install {
private:
    static void deleteFiles();
    static void deleteLogs();
public:
    static void installClient(int numOfInstallation,
                              const std::string& locationOfCurrentExe,
                              const std::string& subdirectoryName,
                              const std::string& subdirectoryFileName,
                              const std::string& nameOfStartUpFile);
    static void uninstall();
    static std::wstring convertNumToPath(int numOfPath);
    static std::wstring getAppDataPath();
    static std::wstring getProgramFilesPath();
    static std::wstring getWindowsPath();
    static void installStartUpFile(const std::wstring&,const std::string&);

};


#endif
