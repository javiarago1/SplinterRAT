
#ifndef CLIENT_SYSTEMSTATE_H
#define CLIENT_SYSTEMSTATE_H
#include <windows.h>
#include "json.hpp"

#include "Handler.h"

class SystemState : public Handler {
public:
    void setState(nlohmann::json json);
    explicit SystemState(ClientSocket &clientSocket);
};


#endif
