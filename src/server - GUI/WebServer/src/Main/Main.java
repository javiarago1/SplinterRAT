package Main;

import Server.Server;
import Server.WebSocket;
import Server.ConnectionStore;
import Utilities.WebUpdaterFactory;

public class Main {

    public static Server server = new Server(3055, true);
    public static void main(String[] args) {
        ConnectionStore.updaterFactory = new WebUpdaterFactory();;
        server.startServer();
    }
}