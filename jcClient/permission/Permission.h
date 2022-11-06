#ifndef CLIENT_PERMISSION_H
#define CLIENT_PERMISSION_H
#include <windows.h>

class Permission {
public:
    static BOOL hasAdminPermission();
    static BOOL elevatePermissions();
};


#endif
