package Main;

import javax.swing.*;

import Server.ConnectionStore;
import Server.Server;
import Server.WebSocket;
import Utilities.SwingUpdaterFactory;
import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.*;

public class Main {

    public static SplinterGUI gui;

    public static Server server;
    private static final int defaultPort = 3055;
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        UIManager.put("Component.focusedBorderColor", new Color(55, 55, 55));
        ConnectionStore.updaterFactory = new SwingUpdaterFactory();
        try {
            server = new Server(defaultPort);
            SwingUtilities.invokeLater(() -> gui = new SplinterGUI());
        } catch (Exception e) {
            System.err.println("Server.Server exception!\n");
            e.printStackTrace();
        }

    }

}