package GUI.TableUtils.FileManager.Actions;

import GUI.TableUtils.FileManager.Event.RunEvent;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class RunAction extends Manager {

    public RunAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getStream().getExecutor().submit(new RunEvent(getStream(), getSelectedPaths(), getFileManagerGUI().getFileManagerDialog()));
    }
}
