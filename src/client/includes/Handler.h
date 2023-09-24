#ifndef CLIENT_HANDLER_H
#define CLIENT_HANDLER_H

#include "ThreadGen.h"
#include "ClientSocket.h"

class Handler {
public:
    ThreadGen threadGen;
    ClientSocket &clientSocket;
    explicit Handler(ClientSocket &clientSocket);
};


#endif
