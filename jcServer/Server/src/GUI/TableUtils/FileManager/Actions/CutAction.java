package GUI.TableUtils.FileManager.Actions;

import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class CutAction extends Manager {

    public CutAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getFileManagerGUI().setCopySelected(false);
        getFileManagerGUI().setCutSelected(true);
        getFileManagerGUI().setCMElements(getSelectedPaths());
    }
}
