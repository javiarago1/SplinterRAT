//
// Created by JAVIER on 05/12/2022.
//

#include "Sender.h"

Sender::Sender(const Stream &stream, std::unordered_map<std::string, std::function<void(nlohmann::json &)>>& actionMap) : stream(stream) {}


