package GUI.TableUtils.FileManager.Actions;

import GUI.TableUtils.FileManager.Events.RunFileManagerEvent;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class RunAction extends FileManagerAbstractAction {

    public RunAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getClient().getExecutor().submit(new RunFileManagerEvent(getGUIManager(), getSelectedPaths()));
    }
}
