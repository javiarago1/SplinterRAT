package GUI.TableUtils.FileManager.Actions;

import GUI.TableUtils.FileManager.Event.DeleteEvent;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class DeleteAction extends Manager {

    public DeleteAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getStream().getExecutor().submit(new DeleteEvent(getStream(), getSelectedPaths(), getFileManagerGUI().getFileManagerDialog()));
    }
}
