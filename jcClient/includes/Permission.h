#ifndef CLIENT_PERMISSION_H
#define CLIENT_PERMISSION_H
#include "Sender.h"
#include <windows.h>


class Permission : public Sender {
public:
    void send() override;

    explicit Permission(const Stream &stream);
    static BOOL hasAdminPermission();
    static BOOL elevatePermissions();
    void sendElevatedPermissions();
};


#endif
