package GUI.TableUtils.FileManager.Actions;

import Connections.Client;
import GUI.TableUtils.FileManager.Events.MoveFileManagerEvent;
import GUI.TableUtils.FileManager.Events.PasteFileManagerEvent;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class PasteAction extends FileManagerAbstractAction {

    public PasteAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = getGUIManager().getClient();
        if (getGUIManager().isCopySelected()) {
            getGUIManager().setCopySelected(false);
            client.getExecutor().submit(new PasteFileManagerEvent(
                    getGUIManager(), getGUIManager().getCMElements(), getSelectedPaths()));
        } else {
            getGUIManager().setCutSelected(false);
            client.getExecutor().submit(new MoveFileManagerEvent(getGUIManager(), getGUIManager().getCMElements(),
                    getSelectedPaths().get(0)));
        }


    }
}
