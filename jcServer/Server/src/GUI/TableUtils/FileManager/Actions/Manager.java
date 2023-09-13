package GUI.TableUtils.FileManager.Actions;

import Connections.Streams;
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
        if (selectedRows.length == 0) {
            listOfPaths.add(fileManagerGUI.getStack().peek());
        } else {
            for (int e : selectedRows) {
                listOfPaths.add(fileManagerGUI.getPathField().getText() + table.getValueAt(e, 0));
            }
        }
        return listOfPaths;

    }


    public String getSelectedPath(){
        JTable table = fileManagerGUI.getTable();
        int selectedRow = table.getSelectedRow();
        String path;
        if (selectedRow == -1) {
            path = fileManagerGUI.getStack().peek();
        } else {
            path = fileManagerGUI.getPathField().getText() + table.getValueAt(selectedRow, 0);

        }
        return path;
    }



    public FileManagerGUI getFileManagerGUI() {
        return fileManagerGUI;
    }
}
