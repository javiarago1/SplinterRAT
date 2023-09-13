//
// Created by Nitropc on 31/08/2023.
//

#include "Information.h"

void Information::sendSystemInformation(){
    //stream.sendList(SystemInformation::getSystemInformation());
}

void Information::sendNetworkInformation(){
    //stream.sendString(NetworkInformation::getNetworkInformation().c_str());
}

void Information::sendSystemAndNetworkInformation(){
    nlohmann::json sysInfo = SystemInformation::getSystemInformation();
    nlohmann::json netInfo = NetworkInformation::getNetworkInformation();
    for (nlohmann::json::iterator it = netInfo.begin(); it != netInfo.end(); ++it) {
        sysInfo[it.key()] = it.value();
    }
    sysInfo["response"] = "sys_net_info";
    clientSocket.sendMessage(sysInfo.dump());
}

Information::Information(ClientSocket &clientSocket) : clientSocket(clientSocket){
    std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap = clientSocket.getActionMap();
    actionMap["SYSTEM"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &Information::sendSystemInformation);
    };
    actionMap["NETWORK"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this,&Information::sendNetworkInformation);
    };
    actionMap["sys_net_info"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &Information::sendSystemAndNetworkInformation);
    };
}


