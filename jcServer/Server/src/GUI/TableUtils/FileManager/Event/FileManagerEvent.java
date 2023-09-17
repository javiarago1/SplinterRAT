package GUI.TableUtils.FileManager.Event;

import Connections.Client;
import GUI.TableUtils.FileManager.FileManagerGUI;
import Information.AbstractEvent;

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
