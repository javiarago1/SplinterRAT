#ifndef CLIENT_PERMISSION_H
#define CLIENT_PERMISSION_H
#include "../sender/Sender.h"
#include <windows.h>


class Permission : public Sender {
public:
    explicit Permission(const Stream &stream);

    static BOOL hasAdminPermission();
    static BOOL elevatePermissions();
    void sendElevatedPermissions();
    void send() override;
};


#endif
