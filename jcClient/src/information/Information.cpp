//
// Created by Nitropc on 31/08/2023.
//

#include "Information.h"

void Information::sendSystemInformation(){
    stream.sendList(SystemInformation::getSystemInformation());
}

void Information::sendNetworkInformation(){
    stream.sendString(NetworkInformation::getNetworkInformation().c_str());
}

Information::Information(const Stream &stream,
                         std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap)
                            : Sender(stream, actionMap) {
    actionMap["SYSTEM"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &Information::sendSystemInformation);
    };
    actionMap["NETWORK"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this,&Information::sendNetworkInformation);
    };
    actionMap["ALL"] = [&](nlohmann::json& json) {
       // threadGen.runInNewThread(this, &MessageBoxGUI::generateMessageBox, json);
    };


}

void Information::send() {

}
