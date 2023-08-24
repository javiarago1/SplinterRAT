package Connections;

import GUI.TableUtils.Configuration.SocketType;

import java.net.Socket;

public record Identifier(String UUID, Streams stream, SocketType socketType, Socket socket) {

}
