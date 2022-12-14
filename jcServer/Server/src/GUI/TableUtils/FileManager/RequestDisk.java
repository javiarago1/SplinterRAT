package GUI.TableUtils.FileManager;

import Connections.ClientErrorHandler;
import Information.Action;

import javax.swing.*;
import java.io.IOException;

public class RequestDisk extends SwingWorker<Void, Void> {


    private final FileManagerGUI fileManagerGUI;

    public RequestDisk(FileManagerGUI fileManager) {
        fileManagerGUI = fileManager;
    }

    private String[] disks;

    @Override
    protected Void doInBackground() {
        try {
            System.out.println(fileManagerGUI.getStream());
            disks = (String[]) fileManagerGUI.getStream().sendAction(Action.DISK);
        } catch (IOException e) {
            new ClientErrorHandler("Unable get disks, connection lost with client",
                    fileManagerGUI.getFileManagerDialog(), fileManagerGUI.getStream().getClientSocket());
        }
        return null;
    }

    @Override
    protected void done() {
        if (disks != null) {
            JComboBox<String> diskBox = fileManagerGUI.getDiskComboBox();
            DefaultComboBoxModel<String> boxModel = (DefaultComboBoxModel<String>) diskBox.getModel();
            for (String e : disks) {
                if (boxModel.getIndexOf(e) == -1) {
                    diskBox.addItem(e);
                }
            }
        }
    }
}
