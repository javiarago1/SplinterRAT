#ifndef CLIENT_CONNECTIONSTATE_H
#define CLIENT_CONNECTIONSTATE_H

#include "Sender.h"
#include "Install.h"
#include "Handler.h"

class ConnectionState : public Handler{
private:

public:
    explicit ConnectionState(ClientSocket &clientSocket,
                             bool &connectionState,
                             bool &streamListening);
};


#endif
