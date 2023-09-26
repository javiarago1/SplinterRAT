#ifndef CLIENT_INFORMATION_H
#define CLIENT_INFORMATION_H



#include "SystemInformation.h"
#include "NetworkInformation.h"
#include "Handler.h"

class Information : Handler {
public:
    void sendSystemInformation();
    void sendNetworkInformation();
    explicit Information(ClientSocket &clientSocket);
    void sendSystemAndNetworkInformation();
};


#endif
