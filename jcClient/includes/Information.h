#ifndef CLIENT_INFORMATION_H
#define CLIENT_INFORMATION_H


#include "Sender.h"
#include "SystemInformation.h"
#include "NetworkInformation.h"
#include "ClientSocket.h"
#include "ThreadGen.h"

class Information {
public:
    ThreadGen threadGen;
    ClientSocket &clientSocket;
    void sendSystemInformation();
    void sendNetworkInformation();
    explicit Information(ClientSocket &clientSocket);
    void sendSystemAndNetworkInformation();
};


#endif
