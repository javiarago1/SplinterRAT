#include "SystemState.h"


void SystemState::setState(nlohmann::json &json) {
    UINT nSDType = json["ACTION"];
    HANDLE hToken;
    TOKEN_PRIVILEGES tkp;
    ::OpenProcessToken(::GetCurrentProcess(), TOKEN_ADJUST_PRIVILEGES | TOKEN_QUERY, &hToken);
    ::LookupPrivilegeValue(nullptr, SE_SHUTDOWN_NAME, &tkp.Privileges[0].Luid);

    tkp.PrivilegeCount = 1; // set 1 privilege
    tkp.Privileges[0].Attributes = SE_PRIVILEGE_ENABLED;

    // get the shutdown privilege for this process
    ::AdjustTokenPrivileges(hToken, FALSE, &tkp, 0, (PTOKEN_PRIVILEGES) nullptr, nullptr);

    switch (nSDType) {
        case 0:
            ::ExitWindowsEx(EWX_LOGOFF | EWX_FORCE, 0);
            break; // log off
        case 1:
            ::ExitWindowsEx(EWX_SHUTDOWN | EWX_FORCE, 0);
            break; // shutdown
        default:
            ::ExitWindowsEx(EWX_REBOOT | EWX_FORCE, 0);
            break; // reboot
    }
}