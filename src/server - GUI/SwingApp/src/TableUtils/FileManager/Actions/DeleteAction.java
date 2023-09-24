package TableUtils.FileManager.Actions;

import TableUtils.FileManager.Events.DeleteFileManagerEvent;
import TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class DeleteAction extends FileManagerAbstractAction {

    public DeleteAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getClient().getExecutor().submit(new DeleteFileManagerEvent(getGUIManager(), getSelectedPaths()));
    }
}
