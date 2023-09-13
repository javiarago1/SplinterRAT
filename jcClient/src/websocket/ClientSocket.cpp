#include "ClientSocket.h"

ClientSocket::ClientSocket(const std::string &host, std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap) :
    actionMap(actionMap) {
    //c.set_open_handler(std::bind(&ClientSocket::on_connection, this, std::placeholders::_1));
    c.set_message_handler(std::bind(&ClientSocket::on_message, this, std::placeholders::_1, std::placeholders::_2));
    c.init_asio();
    // Initialize connection
    websocketpp::lib::error_code ec;
    con = c.get_connection(host, ec);
    if (ec) {
        std::cout << "Error: " << ec.message() << std::endl;
        // Consider terminating here if the initialization fails
    }
}

void ClientSocket::on_message(websocketpp::connection_hdl hdl, client::message_ptr msg) {
    if (msg) {
        jsonObject =  nlohmann::json::parse(std::string(msg->get_payload()));
        std::string action = jsonObject["action"];
        std::cout << "Action: " << action << std::endl;
        auto it = actionMap.find(action);
        if (it != actionMap.end()) {
            it->second(jsonObject);
        } else {
            std::cout << "ACTION NOT FOUND :(" << std::endl;
        }
    }
}
/*
void ClientSocket::on_connection(websocketpp::connection_hdl hdl) {
    if (hdl.lock()) { // Check if the handle is valid
        c.send(hdl, "Hello World", websocketpp::frame::opcode::text);
    }
}*/

void ClientSocket::sendMessage(const std::string &message) {
    std::unique_lock<std::mutex> lock(sendMutex);
    c.send(con->get_handle(), message, websocketpp::frame::opcode::text);
    lock.unlock();
}



void ClientSocket::startConnection() {
    if (con) {
        c.connect(con);
        c.run();
    }
}

std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &ClientSocket::getActionMap() const {
    return actionMap;
}
