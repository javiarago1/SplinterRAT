#ifndef CLIENT_PERMISSION_H
#define CLIENT_PERMISSION_H
#include "Sender.h"
#include <windows.h>


class Permission : public Sender {
public:
    void send() override;

    explicit Permission(const Stream &stream, std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap);
    static BOOL hasAdminPermission();
    static BOOL elevatePermissions();
    void sendElevatedPermissions();
};


#endif
