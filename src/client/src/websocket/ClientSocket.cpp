#include "ClientSocket.h"
#include "SystemInformation.h"
#include "NetworkInformation.h"

ClientSocket::ClientSocket(const std::string &host, ActionMap actionMap) :
    actionMap(actionMap) {
    c.set_open_handler(std::bind(&ClientSocket::on_connection, this, std::placeholders::_1));
    c.set_message_handler(std::bind(&ClientSocket::on_message, this, std::placeholders::_1, std::placeholders::_2));
   // c.set_close_handler(std::bind(&ClientSocket::on_close, this, std::placeholders::_1));
    c.set_fail_handler(std::bind(&ClientSocket::on_fail, this, std::placeholders::_1));
    c.set_access_channels(websocketpp::log::alevel::none);
    c.set_error_channels(websocketpp::log::elevel::none);
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
        if (msg->get_opcode() == websocketpp::frame::opcode::text) {
            // Handle text message
            jsonObject =  nlohmann::json::parse(std::string(msg->get_payload()));
            std::string action = jsonObject["ACTION"];
            std::cout << "Action: " << action << std::endl;
            auto it = actionMap.find(action);
            if (it != actionMap.end()) {
                it->second(jsonObject);
            } else {
                std::cout << "ACTION NOT FOUND :(" << std::endl;
            }
        } else if (msg->get_opcode() == websocketpp::frame::opcode::binary) {
            // Handle binary message
            std::string str_payload = msg->get_raw_payload();
            std::cout << "Length" << str_payload.length() << std::endl;
            std::vector<uint8_t> payload(str_payload.begin(), str_payload.end());
            on_byte_message(payload);
        }
    }
}

void ClientSocket::on_byte_message(const std::vector<uint8_t>& payload) {
    if (payload.size() < 2) {

        return;
    }

    uint8_t id = payload[0];
    uint8_t isLast = payload[1];

    tempBuffers[id].buffer.insert(tempBuffers[id].buffer.end(), payload.begin() + 2, payload.end());

    if (isLast) {
        writeFileFromBuffer(id);
        tempBuffers.erase(id);
    }
}


void ClientSocket::writeFileFromBuffer(uint8_t id) {
    std::cout << "Writing file for ID " << (int)id << std::endl;
   // std::cout << tempBuffers[id].destinationPath + "\\file.zip" << std::endl;
   // std::ofstream outFile(tempBuffers[id].destinationPath + "\\file.zip", std::ios::binary);
   // outFile.write((char *)tempBuffers[id].buffer.data(), tempBuffers[id].buffer.size());
   // outFile.close();
   ZipCompressor::decompressFromMemory(tempBuffers[id].buffer, tempBuffers[id].destinationPath);

}

void ClientSocket::on_fail(websocketpp::connection_hdl hdl) {
    std::cout << "Failed to establish connection." << std::endl;

}



void ClientSocket::on_connection(websocketpp::connection_hdl hdl) {
    if (hdl.lock()) { // Check if the handle is valid
        nlohmann::json sysInfo = SystemInformation::getSystemInformation();
        nlohmann::json netInfo = NetworkInformation::getNetworkInformation();
        for (nlohmann::json::iterator it = netInfo.begin(); it != netInfo.end(); ++it) {
            sysInfo[it.key()] = it.value();
        }
        sysInfo["RESPONSE"] = "SYS_NET_INFO";
        sendMessage(sysInfo.dump());
    }
}

void ClientSocket::sendMessage(const std::string &message) {
    std::unique_lock<std::mutex> lock(sendMutex);
    c.send(con->get_handle(), message, websocketpp::frame::opcode::text);
    lock.unlock();
}

void ClientSocket::sendBytes(const std::vector<uint8_t> &bytes) {
    std::unique_lock<std::mutex> lock(sendMutex);
    c.send(con->get_handle(), bytes.data(), bytes.size(), websocketpp::frame::opcode::binary);
    lock.unlock();
}




void ClientSocket::closeConnection() {
    if (con) {
        websocketpp::lib::error_code ec;
        c.close(con->get_handle(), websocketpp::close::status::normal, "Client closing connection", ec);
        if (ec) {
            std::cout << "Error on close: " << ec.message() << std::endl;
        }
    }
}


void ClientSocket::startConnection() {
    if (con) {
        websocketpp::lib::error_code ec;
        c.connect(con);
        c.run();
    }
}

std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &ClientSocket::getActionMap() const {
    return actionMap;
}
