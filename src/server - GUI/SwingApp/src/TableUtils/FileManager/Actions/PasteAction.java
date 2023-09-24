package TableUtils.FileManager.Actions;

import Server.Client;
import TableUtils.FileManager.Events.MoveFileManagerEvent;
import TableUtils.FileManager.Events.PasteFileManagerEvent;
import TableUtils.FileManager.FileManagerGUI;

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
