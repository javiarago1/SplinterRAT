package GUI.TableUtils.FileManager.Listeners;

import GUI.TableUtils.FileManager.FileManagerGUI;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

public class MouseListener extends MouseAdapter {

    private final FileManagerGUI fileManagerGUI;

    private final Stack<String> stack;


    public MouseListener(FileManagerGUI fileManagerGUI) {
        this.stack = fileManagerGUI.getStack();
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
                } else {
                    stack.pop();
                    fileManagerGUI.requestDirectory(stack.peek());
                }
            } else if (row <= fileManagerGUI.getDivider()) {
                String path = stack.isEmpty() ? table.getValueAt(row, column) + "\\" :stack.peek() + table.getValueAt(row, column) + "\\";
                stack.push(path);
                fileManagerGUI.requestDirectory(path);
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
