package GUI.TableUtils.FileManager.Actions;

import Connections.Streams;
import GUI.TableUtils.FileManager.Event.MoveEvent;
import GUI.TableUtils.FileManager.Event.PasteEvent;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class PasteAction extends Manager {

    public PasteAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = getFileManagerGUI().getStream();
        if (getFileManagerGUI().isCopySelected()) {
            getFileManagerGUI().setCopySelected(false);
            getFileManagerGUI().getStream().getExecutor().submit(new PasteEvent(
                    stream, getFileManagerGUI().getCMElements(), getSelectedPaths(), getFileManagerGUI().getFileManagerDialog()));
        } else {
            getFileManagerGUI().setCutSelected(false);
            getFileManagerGUI().getStream().getExecutor().submit(new MoveEvent(
                    getFileManagerGUI().getStream(), getFileManagerGUI().getCMElements(),
                    getSelectedPaths().get(0), getFileManagerGUI().getFileManagerDialog()));
        }


    }
}
