#include "ConnectionState.h"


ConnectionState::ConnectionState(const Stream &stream, std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap,
                                 bool &connectionState,
                                 bool &streamListening) :
                                 Sender(stream, actionMap)
                                 {
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

void ConnectionState::send() {

}
