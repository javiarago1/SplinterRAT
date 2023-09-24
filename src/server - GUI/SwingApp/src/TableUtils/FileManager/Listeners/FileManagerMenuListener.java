package TableUtils.FileManager.Listeners;


import TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Main.SplinterGUI;

import Utilities.SwingUpdater;

import Server.Client;

import Utilities.GetSYS;


public class FileManagerMenuListener implements ActionListener {

    private final SplinterGUI mainGUI;

    public FileManagerMenuListener(SplinterGUI gui) {
        this.mainGUI = gui;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        FileManagerGUI fileManagerGUI = new FileManagerGUI(client);
        SwingUpdater swingUpdater = (SwingUpdater) client.updater;
        swingUpdater.setFileManagerGUI(fileManagerGUI);
        fileManagerGUI.requestDisks();
    }
}