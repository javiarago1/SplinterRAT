//
// Created by JAVIER on 05/12/2022.
//

#include "Sender.h"

Sender::Sender(const Stream &stream) : stream(stream) {}

void Sender::managerMenu(nlohmann::json& json){
    auto it = actionMap.find(json["ACTION"]);
    if (it != actionMap.end()) {
        it->second(json);
    } else {
        std::cout << "Not found :( " << json["ACTION"] << actionMap.size()<< std::endl;
    }
}

