#include "ConnectionState.h"


ConnectionState::ConnectionState(ClientSocket &clientSocket,
                                 bool &connectionState,
                                 bool &streamListening) :
                                 Handler(clientSocket)
                                 {
    ActionMap& actionMap = clientSocket.getActionMap();
    actionMap["UNINSTALL"] = [](nlohmann::json& json) {
        Install::uninstall();
    };
    actionMap["DISCONNECT"] = [&](nlohmann::json& json) {
        connectionState = false;
        streamListening = false;
    };
    actionMap["RECONNECT"] = [&](nlohmann::json& json) {
        streamListening = false;
    };
}
