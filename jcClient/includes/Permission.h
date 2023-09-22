#ifndef CLIENT_PERMISSION_H
#define CLIENT_PERMISSION_H
#include "Sender.h"
#include <windows.h>
#include "Handler.h"


class Permission : public Handler {
public:
    explicit Permission(ClientSocket& clientSocket);
    static BOOL hasAdminPermission();
    static BOOL elevatePermissions();
    void sendElevatedPermissions();
};


#endif
