package GUI.TableUtils.FileManager.Actions;

import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class CutAction extends FileManagerAbstractAction {

    public CutAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getGUIManager().setCopySelected(false);
        getGUIManager().setCutSelected(true);
        getGUIManager().setCMElements(getSelectedPaths());
    }
}
