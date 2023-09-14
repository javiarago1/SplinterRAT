package GUI.TableUtils.FileManager.Listener;

import Connections.Client;
import Connections.ClientHandler;
import Connections.Streams;
import GUI.SplinterGUI;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

public class FileManagerMenuListener implements ActionListener {

    private final SplinterGUI mainGUI;

    public FileManagerMenuListener(SplinterGUI gui) {
        this.mainGUI = gui;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandlerV2();
        assert client != null;
        FileManagerGUI fileManagerGUI = new FileManagerGUI(client, mainGUI.getMainGUI());
        client.setFileManagerGUI(fileManagerGUI);
        fileManagerGUI.requestDisks();
    }
}