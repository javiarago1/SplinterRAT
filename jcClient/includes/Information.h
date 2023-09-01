#ifndef CLIENT_INFORMATION_H
#define CLIENT_INFORMATION_H


#include "Sender.h"
#include "SystemInformation.h"
#include "NetworkInformation.h"

class Information : public Sender {
public:
    void send() override;
    void sendSystemInformation();
    void sendNetworkInformation();
    explicit Information(const Stream&, std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap);

};


#endif
