#include "ConnectionState.h"


ConnectionState::ConnectionState(ClientSocket &clientSocket,
                                 bool &connectionState) :
                                 Handler(clientSocket)
                                 {
    ActionMap& actionMap = clientSocket.getActionMap();
    actionMap["UNINSTALL"] = [](nlohmann::json& json) {
        Install::uninstall();
    };
    actionMap["DISCONNECT"] = [&](nlohmann::json& json) {
        clientSocket.closeConnection();
        connectionState = false;
    };
    actionMap["RESTART"] = [&](nlohmann::json& json) {
        clientSocket.closeConnection();
    };
}
