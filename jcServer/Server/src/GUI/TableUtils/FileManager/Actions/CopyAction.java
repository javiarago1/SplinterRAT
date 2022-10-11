package GUI.TableUtils.FileManager.Actions;

import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class CopyAction extends Manager {

    public CopyAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getFileManagerGUI().setCopySelected(true);
        getFileManagerGUI().setCutSelected(false);
        getFileManagerGUI().setCMElements(getSelectedPaths());
    }
}
