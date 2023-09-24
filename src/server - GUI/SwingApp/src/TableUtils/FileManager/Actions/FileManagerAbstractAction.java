package TableUtils.FileManager.Actions;

import TableUtils.FileManager.FileManagerGUI;
import Utilities.AbstractAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class FileManagerAbstractAction extends AbstractAction<FileManagerGUI> {

    public FileManagerAbstractAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);

    public List<String> getSelectedPaths() {
        JTable table = getGUIManager().getTable();
        int[] selectedRows = table.getSelectedRows();
        List<String> listOfPaths = new ArrayList<>();
        if (selectedRows.length == 0) {
            listOfPaths.add(getGUIManager().getStack().peek());
        } else {
            for (int e : selectedRows) {
                listOfPaths.add(getGUIManager().getPathField().getText() + table.getValueAt(e, 0));
            }
        }
        return listOfPaths;

    }


    public String getSelectedPath(){
        JTable table = getGUIManager().getTable();
        int selectedRow = table.getSelectedRow();
        String path;
        if (selectedRow == -1) {
            path = getGUIManager().getStack().peek();
        } else {
            path = getGUIManager().getPathField().getText() + table.getValueAt(selectedRow, 0);

        }
        return path;
    }

}
