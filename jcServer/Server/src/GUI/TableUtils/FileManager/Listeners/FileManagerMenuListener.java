package GUI.TableUtils.FileManager.Listeners;

import Connections.Client;
import GUI.SplinterGUI;
import Connections.GetSYS;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileManagerMenuListener implements ActionListener {

    private final SplinterGUI mainGUI;

    public FileManagerMenuListener(SplinterGUI gui) {
        this.mainGUI = gui;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandlerV2();
        assert client != null;
        FileManagerGUI fileManagerGUI = new FileManagerGUI(client);
        client.setFileManagerGUI(fileManagerGUI);
        fileManagerGUI.requestDisks();
    }
}