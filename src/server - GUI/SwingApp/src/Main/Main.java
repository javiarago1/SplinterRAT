package Main;

import javax.swing.*;

import Server.ConnectionStore;
import Server.Server;
import Utilities.SwingUpdaterFactory;

public class Main {

    public static SplinterGUI gui;

    public static Server server;
    private static final int defaultPort = 3055;
    public static void main(String[] args) {
        ConnectionStore.updaterFactory = new SwingUpdaterFactory();
        try {
            server = new Server(defaultPort);
            server.startServer();
            SwingUtilities.invokeLater(() -> gui = new SplinterGUI());
        } catch (Exception e) {
            System.err.println("Server.Server exception!\n");
            e.printStackTrace();
        }

    }

}