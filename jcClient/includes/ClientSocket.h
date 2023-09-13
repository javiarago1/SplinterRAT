#ifndef CLIENT_SOCKET
#define CLIENT_SOCKET
#include <websocketpp/config/asio_no_tls_client.hpp>
#include <websocketpp/client.hpp>
#include <iostream>
#include <mutex>
#include "json.hpp"

class ClientSocket {
private:
    nlohmann::json jsonObject;
    std::unordered_map<std::string, std::function<void(nlohmann::json &)>>& actionMap;
    typedef websocketpp::client<websocketpp::config::asio_client> client;
    client c;
    client::connection_ptr con;
    std::mutex sendMutex;
public:
    explicit ClientSocket(const std::string& host, std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap);
    void on_message(websocketpp::connection_hdl hdl, client::message_ptr msg);
    void on_connection(websocketpp::connection_hdl hdl);
    void startConnection();
    void sendMessage(const std::string &message);
    std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &getActionMap() const;
};

#endif