//
// Created by JAVIER on 05/12/2022.
//

#ifndef CLIENT_SENDER_H
#define CLIENT_SENDER_H

#include "../stream/Stream.h"

class Sender {

public:
    Stream stream;
    explicit Sender(const Stream &stream);
    virtual void send() = 0;


};

#endif