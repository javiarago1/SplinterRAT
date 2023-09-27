package TableUtils.FileManager.Events;

import TableUtils.FileManager.FileManagerGUI;
import Utilities.Event.AbstractEventGUI;

import java.util.List;

public abstract class FileManagerEvent extends AbstractEventGUI<FileManagerGUI> {
    private final List<String> CMElements;

    public FileManagerEvent(FileManagerGUI fileManagerGUI, List<String> CMElements) {
        super(fileManagerGUI);
        this.CMElements = CMElements;
    }
    public List<String> getCMElements() {
        return CMElements;
    }

}
