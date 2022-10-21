package GUI.TableUtils.FileManager.Event;

import Connections.ClientErrorHandler;
import Connections.Streams;
import Information.Action;

import javax.swing.*;
import java.util.List;

public class MoveEvent extends Event {
    private final String directoryToMove;
    private final JDialog fileManagerDialog;

    public MoveEvent(Streams stream, List<String> CMElements, String directoryToMove, JDialog fileManagerDialog) {
        super(stream, CMElements);
        this.directoryToMove = directoryToMove;
        this.fileManagerDialog = fileManagerDialog;
    }

    @Override
    public void run() {
        try {
            getStream().sendAndReadJSON(Action.MOVE, getCMElements(), directoryToMove);
        } catch (Exception ex) {
            new ClientErrorHandler("Unable to move, connection lost with client", fileManagerDialog);
        }
    }
}
