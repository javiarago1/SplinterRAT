package GUI.TableUtils.FileManager.Actions;

import Connections.Client;
import GUI.TableUtils.FileManager.Event.DeleteEvent;
import GUI.TableUtils.FileManager.Event.RunEvent;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class RunAction extends Manager {

    public RunAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = getFileManagerGUI().getClient();
        getFileManagerGUI().getClient().getExecutor().submit(new RunEvent(client, getSelectedPaths(), getFileManagerGUI().getFileManagerDialog()));    }
}
