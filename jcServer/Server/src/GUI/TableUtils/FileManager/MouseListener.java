package GUI.TableUtils.FileManager;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

public class MouseListener extends MouseAdapter {

    private final FileManagerGUI fileManagerGUI;

    public MouseListener(FileManagerGUI fileManagerGUI) {
        this.fileManagerGUI = fileManagerGUI;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {     // to detect doble click events
            JTable target = (JTable) e.getSource();
            int row = target.getSelectedRow(); // select a row
            int column = target.getSelectedColumn();
            if (row == 0) {
                Stack<String> stack = fileManagerGUI.getStack();
                stack.pop();
                fileManagerGUI.requestDirectory(stack.peek(), true);
            } else if (row <= fileManagerGUI.getDivider()) {
                fileManagerGUI.requestDirectory((String) target.getValueAt(row, column), false);
            } else {
                JOptionPane.showMessageDialog(null, "Is file ");
            }
        }
    }
}
