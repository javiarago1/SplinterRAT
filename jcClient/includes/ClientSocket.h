#include <websocketpp/config/asio_no_tls_client.hpp>
#include <websocketpp/client.hpp>
#include <iostream>

class ClientSocket {
private:
    typedef websocketpp::client<websocketpp::config::asio_client> client;
    client c;
    client::connection_ptr con;
public:
    explicit ClientSocket(const std::string& host);
    void on_message(websocketpp::connection_hdl hdl, client::message_ptr msg);
    void on_connection(websocketpp::connection_hdl hdl);
    void startConnection();
};
