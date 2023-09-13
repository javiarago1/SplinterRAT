package GUI.TableUtils.FileManager;

import Connections.ClientErrorHandler;
import Information.Action;
import org.json.JSONObject;

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
            System.out.println("Disk requested");
            JSONObject jsonObject = new JSONObject("action", "send_disks");
            fileManagerGUI.getClient().sendMessage(jsonObject.toString());
        } catch (IOException e) {
            // new ClientErrorHandler("Unable get disks, connection lost with client",
            //       fileManagerGUI.getFileManagerDialog(), fileManagerGUI.getStream().getClientSocket());
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
