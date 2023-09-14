package GUI.TableUtils.FileManager.Actions;

import Connections.Client;
import Connections.Streams;
import GUI.TableUtils.FileManager.Event.DeleteEvent;
import GUI.TableUtils.FileManager.Event.MoveEvent;
import GUI.TableUtils.FileManager.Event.PasteEvent;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class PasteAction extends Manager {

    public PasteAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = getFileManagerGUI().getClient();
        if (getFileManagerGUI().isCopySelected()) {
            getFileManagerGUI().setCopySelected(false);
            client.getExecutor().submit(new PasteEvent(
                    client, getFileManagerGUI().getCMElements(), getSelectedPaths(), getFileManagerGUI().getFileManagerDialog()));
        } else {
            getFileManagerGUI().setCutSelected(false);
            client.getExecutor().submit(new MoveEvent(client, getFileManagerGUI().getCMElements(),
                    getSelectedPaths().get(0), getFileManagerGUI().getFileManagerDialog()));
        }


    }
}
