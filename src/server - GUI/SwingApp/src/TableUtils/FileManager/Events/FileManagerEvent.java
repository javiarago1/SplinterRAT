package TableUtils.FileManager.Events;

import TableUtils.FileManager.FileManagerGUI;
import Utilities.AbstractEvent;

import java.util.List;

public abstract class FileManagerEvent extends AbstractEvent<FileManagerGUI> {
    private final List<String> CMElements;

    public FileManagerEvent(FileManagerGUI fileManagerGUI, List<String> CMElements) {
        super(fileManagerGUI);
        this.CMElements = CMElements;
    }
    public List<String> getCMElements() {
        return CMElements;
    }

}
