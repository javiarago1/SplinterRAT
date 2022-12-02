package GUI;

import Connections.Server;

import javax.swing.*;
public class Main {

    static public SplinterGUI gui;
    public static Server server;
    private static final int defaultPort = 3055;

    public static void main(String[] args) {
        try {
            server = new Server(defaultPort);
            SwingUtilities.invokeLater(() -> gui = new SplinterGUI());
        } catch (Exception e) {
            System.err.println("Server exception!\n");
            e.printStackTrace();
        }

    }

}