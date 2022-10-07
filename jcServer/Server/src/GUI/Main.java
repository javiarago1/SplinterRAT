package GUI;

import Connections.Server;

import javax.swing.*;
public class Main {

    static public JsGUI gui;

    public static void main(String[] args) {
        
        try {
            Server server = new Server(3055);
            SwingUtilities.invokeLater(() -> gui = new JsGUI(server));
            server.startServer();
        } catch (Exception e) {
            System.err.println("Server exception!\n");
            e.printStackTrace();
        }

    }

}