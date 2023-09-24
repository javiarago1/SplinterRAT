//
// Created by JAVIER on 05/12/2022.
//

#ifndef CLIENT_SENDER_H
#define CLIENT_SENDER_H

#include "Stream.h"
#include "json.hpp"
#include "ThreadGen.h"

class Sender {

public:
    Stream stream;
    ThreadGen threadGen;
    explicit Sender(const Stream &stream, std::unordered_map<std::string, std::function<void(nlohmann::json &)>>& actionMap);
    virtual void send() = 0;


};

#endif