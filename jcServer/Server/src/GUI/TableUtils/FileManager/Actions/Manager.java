package GUI.TableUtils.FileManager.Actions;

import GUI.TableUtils.FileManager.FileManagerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public abstract class Manager implements ActionListener {

    private final FileManagerGUI fileManagerGUI;

    public Manager(FileManagerGUI fileManagerGUI) {
        this.fileManagerGUI = fileManagerGUI;
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);

    public List<String> getSelectedPaths() {
        JTable table = fileManagerGUI.getTable();
        int[] selectedRows = table.getSelectedRows();
        List<String> listOfPaths = new ArrayList<>();
        for (int e : selectedRows) {
            listOfPaths.add(fileManagerGUI.getTextField().getText() + table.getValueAt(e, 0));
        }
        return listOfPaths;

    }

    public FileManagerGUI getFileManagerGUI() {
        return fileManagerGUI;
    }
}
