package GUI.TableUtils.FileManager.Listener;

import Connections.ClientHandler;
import Connections.Streams;
import GUI.SplinterGUI;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class FileManagerMenuListener implements ActionListener {

    private final SplinterGUI mainGUI;

    public FileManagerMenuListener(SplinterGUI gui) {
        this.mainGUI = gui;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ClientHandler clientHandler = GetSYS.getClientHandler();
        assert clientHandler != null;
        new FileManagerGUI(clientHandler, mainGUI.getMainGUI());


    }
}