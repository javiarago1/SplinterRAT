package GUI.TableUtils.FileManager.Actions;

import Connections.Client;
import GUI.TableUtils.FileManager.Event.MoveFileManagerEvent;
import GUI.TableUtils.FileManager.Event.PasteFileManagerEvent;
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
            client.getExecutor().submit(new PasteFileManagerEvent(
                    getFileManagerGUI(), getFileManagerGUI().getCMElements(), getSelectedPaths()));
        } else {
            getFileManagerGUI().setCutSelected(false);
            client.getExecutor().submit(new MoveFileManagerEvent(getFileManagerGUI(), getFileManagerGUI().getCMElements(),
                    getSelectedPaths().get(0)));
        }


    }
}
