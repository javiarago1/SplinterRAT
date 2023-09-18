package GUI.TableUtils.FileManager.Actions;

import GUI.TableUtils.FileManager.Events.DeleteFileManagerEvent;
import GUI.TableUtils.FileManager.FileManagerGUI;

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
