package TableUtils.FileManager.Actions;

import TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class CopyAction extends FileManagerAbstractAction {

    public CopyAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getGUIManager().setCopySelected(true);
        getGUIManager().setCutSelected(false);
        getGUIManager().setCMElements(getSelectedPaths());
    }
}
