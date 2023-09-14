package GUI.TableUtils.FileManager.Actions;

import Connections.Client;
import GUI.TableUtils.FileManager.Event.DeleteEvent;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class DeleteAction extends Manager {

    public DeleteAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = getFileManagerGUI().getClient();
        getFileManagerGUI().getClient().getExecutor().submit(new DeleteEvent(client, getSelectedPaths(), getFileManagerGUI().getFileManagerDialog()));
    }
}
