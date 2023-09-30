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
    sysInfo["RESPONSE"] = "SYS_NET_INFO";
    clientSocket.sendMessage(sysInfo);
}

Information::Information(ClientSocket &clientSocket) : Handler(clientSocket){
    ActionMap actionMap = clientSocket.getActionMap();
    actionMap["SYSTEM"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &Information::sendSystemInformation);
    };
    actionMap["NETWORK"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this,&Information::sendNetworkInformation);
    };
    actionMap["SYS_NET_INFO"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &Information::sendSystemAndNetworkInformation);
    };
}


