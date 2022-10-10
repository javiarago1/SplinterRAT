package GUI.TableUtils.FileManager;

import Information.Action;

import javax.swing.*;

public class RequestDisk extends SwingWorker<Void, Void> {


    private final FileManagerGUI fileManagerGUI;

    public RequestDisk(FileManagerGUI fileManager) {
        fileManagerGUI = fileManager;
    }

    private String[] disks;

    @Override
    protected Void doInBackground() {
        disks = (String[]) fileManagerGUI.getStream().sendAndReadJSON(Action.DISK);
        return null;
    }

    @Override
    protected void done() {
        for (String e : disks) {
            fileManagerGUI.getDiskComboBox().addItem(e);
        }
        String selected = (String) fileManagerGUI.getDiskComboBox().getSelectedItem();
        fileManagerGUI.requestDirectory(selected, false);

    }
}
