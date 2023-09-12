#include "ClientSocket.h"

ClientSocket::ClientSocket(const std::string &host) {
    c.set_open_handler(std::bind(&ClientSocket::on_connection, this, std::placeholders::_1));
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
        std::cout << "Received: " << msg->get_payload() << std::endl;
    }
}

void ClientSocket::on_connection(websocketpp::connection_hdl hdl) {
    if (hdl.lock()) { // Check if the handle is valid
        c.send(hdl, "Hello World", websocketpp::frame::opcode::text);
    }
}

void ClientSocket::startConnection() {
    if (con) {
        c.connect(con);
        c.run();
    }
}
