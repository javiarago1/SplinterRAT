package GUI.TableUtils.FileManager.Listener;

import GUI.TableUtils.FileManager.FileManagerGUI;

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
        int[] rows = table.getSelectedRows();
        if (e.getClickCount() == 2 && rows.length > 0) {
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
        } else if (SwingUtilities.isRightMouseButton(e)) {
            JPopupMenu popupMenu = fileManagerGUI.getPopupMenu();
            popupMenu.getComponent(3).setVisible(fileManagerGUI.isCopySelected() || fileManagerGUI.isCutSelected()
                    && checkSelected(fileManagerGUI, rows));
            popupMenu.getComponent(4).setVisible(checkSelected(fileManagerGUI, rows));
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }

    }

    private boolean checkSelected(FileManagerGUI fileManagerGUI, int[] rows) {
        int divider = fileManagerGUI.getDivider();
        for (int e : rows) {
            if (e > divider) {
                return false;
            }
        }
        return true;
    }

}
