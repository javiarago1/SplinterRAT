
#ifndef CLIENT_SYSTEMSTATE_H
#define CLIENT_SYSTEMSTATE_H
#include <windows.h>
#include "json.hpp"
#include "Sender.h"

class SystemState : public Sender {
public:
    void send() override;
    void setState(nlohmann::json json);
    explicit SystemState(const Stream &stream, std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap);
};


#endif
