#ifndef CLIENT_CONNECTIONSTATE_H
#define CLIENT_CONNECTIONSTATE_H

#include "Sender.h"
#include "Install.h"

class ConnectionState : public Sender{
private:
    bool &connectionState;
    bool &streamListening;
public:
    void send() override;
    explicit ConnectionState(const Stream &stream,
                             bool &connectionState,
                             bool &streamListening);
};


#endif
