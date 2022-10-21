package GUI.TableUtils.FileManager.Event;

import Connections.ClientErrorHandler;
import Connections.Streams;
import Information.Action;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class RunEvent extends Event {

    private final JDialog fileManagerDialog;

    public RunEvent(Streams stream, List<String> CMElements, JDialog fileManagerDialog) {
        super(stream, CMElements);
        this.fileManagerDialog = fileManagerDialog;
    }

    @Override
    public void run() {
        try {
            getStream().sendAndReadJSON(Action.RUN, getCMElements());
        } catch (Exception ex) {
            new ClientErrorHandler("Unable to run, connection lost with client", fileManagerDialog);
        }
    }
}
