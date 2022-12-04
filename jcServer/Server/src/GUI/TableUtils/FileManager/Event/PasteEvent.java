package GUI.TableUtils.FileManager.Event;

import Connections.ClientErrorHandler;
import Connections.Streams;
import Information.Action;

import javax.swing.*;
import java.util.List;

public class PasteEvent extends Event {


    private final List<String> listWhereToPaste;
    private JDialog fileManagerDialog;

    public PasteEvent(Streams stream, List<String> CMElements, List<String> listWhereToPaste, JDialog fileManagerDialog) {
        super(stream, CMElements);
        this.listWhereToPaste = listWhereToPaste;
        this.fileManagerDialog = fileManagerDialog;
    }

    @Override
    public void run() {
        try {
            getStream().sendAction(Action.COPY, getCMElements(), listWhereToPaste);
        } catch (Exception ex) {
            new ClientErrorHandler("Unable to paste, connection lost with client",
                    fileManagerDialog, getStream().getClientSocket());
        }
    }
}
