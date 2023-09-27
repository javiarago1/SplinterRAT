package TableUtils.FileManager.Listeners;


import TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import Main.SplinterGUI;

import Utilities.GUIFactory;
import Utilities.MenuListenerClientAssigner;
import Utilities.SwingUpdater;

import Server.Client;

import Utilities.GetSYS;


public class FileManagerMenuListener extends MenuListenerClientAssigner<FileManagerGUI> {
    public FileManagerMenuListener(GUIFactory<FileManagerGUI> guiFactory) {
        super(guiFactory);
    }
}