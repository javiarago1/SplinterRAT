#ifndef CLIENT_SOCKET
#define CLIENT_SOCKET
#define _WEBSOCKETPP_CPP11_THREAD_
#include <websocketpp/config/asio_no_tls_client.hpp>
#include <websocketpp/client.hpp>
#include <iostream>
#include <mutex>
#include "json.hpp"
#include "ZipCompressor.h"

typedef std::unordered_map<std::string, std::function<void(nlohmann::json &)>>& ActionMap;

struct BufferedData {
    std::vector<uint8_t> buffer;
    std::string destinationPath;
};

class ClientSocket {
private:
    nlohmann::json jsonObject;
    typedef websocketpp::client<websocketpp::config::asio_client> client;
    client c;
    client::connection_ptr con;
    std::mutex sendMutex;
    ActionMap actionMap;
public:
    std::unordered_map<uint8_t, BufferedData> tempBuffers;
    explicit ClientSocket(const std::string& host, ActionMap);
    void on_byte_message(const std::vector<uint8_t>& payload);
    void on_message(websocketpp::connection_hdl hdl, client::message_ptr msg);
    void on_connection(websocketpp::connection_hdl hdl);
    void sendBytes(const std::vector<uint8_t> &bytes);
    void startConnection();
    void sendMessage(nlohmann::json& json);
    void on_close(websocketpp::connection_hdl hdl);
    void on_fail(websocketpp::connection_hdl hdl);
    void restartConnection();
    void closeConnection();
    std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &getActionMap() const;
    void writeFileFromBuffer(uint8_t id);
};

#endif