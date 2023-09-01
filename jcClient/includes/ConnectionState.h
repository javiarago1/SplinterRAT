#ifndef CLIENT_CONNECTIONSTATE_H
#define CLIENT_CONNECTIONSTATE_H

#include "Sender.h"
#include "Install.h"

class ConnectionState : public Sender{
private:

public:
    void send() override;
    explicit ConnectionState(const Stream &stream,std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap,
                             bool &connectionState,
                             bool &streamListening);
};


#endif
