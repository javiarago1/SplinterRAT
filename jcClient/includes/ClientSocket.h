#ifndef CLIENT_SOCKET
#define CLIENT_SOCKET
#include <websocketpp/config/asio_no_tls_client.hpp>
#include <websocketpp/client.hpp>
#include <iostream>
#include <mutex>
#include "json.hpp"

typedef std::unordered_map<std::string, std::function<void(nlohmann::json &)>>& ActionMap;

class ClientSocket {
private:
    nlohmann::json jsonObject;

    typedef websocketpp::client<websocketpp::config::asio_client> client;
    client c;
    client::connection_ptr con;
    std::mutex sendMutex;
    ActionMap actionMap;
public:
    explicit ClientSocket(const std::string& host, ActionMap);
    void on_message(websocketpp::connection_hdl hdl, client::message_ptr msg);
    void on_connection(websocketpp::connection_hdl hdl);
    void sendBytes(const std::vector<uint8_t> &bytes);
    void startConnection();
    void sendMessage(const std::string &message);
    std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &getActionMap() const;
};

#endif