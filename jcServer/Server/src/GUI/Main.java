package GUI;

import Connections.Server;
import Connections.ServerV2;

import javax.swing.*;
public class Main {

    static public SplinterGUI gui;
    public static Server server;

    public static ServerV2 serverV2;
    private static final int defaultPort = 3055;

    public static void main(String[] args) {
        try {
            serverV2 = new ServerV2();
            serverV2.startServer();
            server = new Server(defaultPort);
            SwingUtilities.invokeLater(() -> gui = new SplinterGUI());
        } catch (Exception e) {
            System.err.println("Server exception!\n");
            e.printStackTrace();
        }

    }

}