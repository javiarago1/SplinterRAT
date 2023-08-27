#include "ConnectionState.h"


ConnectionState::ConnectionState(const Stream &stream,
                                 bool &connectionState,
                                 bool &streamListening) :
                                 Sender(stream),
                                 connectionState(connectionState),
                                 streamListening(streamListening)
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
