package GUI;

import Connections.Server;
import GUI.Server.ServerGUI;

import javax.swing.*;
public class Main {

    static public JsGUI gui;
    public static Server server;

    public static void main(String[] args) {
        try {
            server = new Server(4040);
            SwingUtilities.invokeLater(() -> gui = new JsGUI());
        } catch (Exception e) {
            System.err.println("Server exception!\n");
            e.printStackTrace();
        }

    }

}