package GUI.TableUtils.FileManager.Event;

import Connections.ClientErrorHandler;
import Connections.Streams;
import Information.Action;

import javax.swing.*;
import java.util.List;

public class DeleteEvent extends Event {

    private final JDialog fileManagerDialog;

    public DeleteEvent(Streams stream, List<String> CMElements, JDialog fileManagerDialog) {
        super(stream, CMElements);
        this.fileManagerDialog = fileManagerDialog;
    }

    @Override
    public void run() {
        try {
            getStream().sendAndReadJSON(Action.DELETE, getCMElements());
        } catch (Exception e) {
            new ClientErrorHandler("Unable to delete, connection lost with client", fileManagerDialog);
        }
    }
}
