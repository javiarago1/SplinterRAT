package GUI.TableUtils.FileManager.Actions;

import Connections.Client;
import GUI.TableUtils.FileManager.Event.RunFileManagerEvent;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class RunAction extends Manager {

    public RunAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getFileManagerGUI().getClient().getExecutor().submit(new RunFileManagerEvent(getFileManagerGUI(), getSelectedPaths()));
    }
}
