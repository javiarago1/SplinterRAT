package GUI.TableUtils.FileManager;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener extends MouseAdapter {

    private final FileManagerGUI fileManagerGUI;

    public MouseListener(FileManagerGUI fileManagerGUI) {
        this.fileManagerGUI = fileManagerGUI;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
            int row = table.getSelectedRow();
            int column = table.getSelectedColumn();
            if (row == 0) {
                if (fileManagerGUI.getStack().size() == 1) {
                    JOptionPane.showMessageDialog(null, "No more folders to show",
                            "Nothing to show", JOptionPane.ERROR_MESSAGE);
                } else fileManagerGUI.requestDirectory();
            } else if (row <= fileManagerGUI.getDivider()) {
                fileManagerGUI.requestDirectory((String) table.getValueAt(row, column));
            }
        }
    }
}
