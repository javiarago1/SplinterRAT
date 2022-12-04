package GUI.TableUtils.FileManager;

import Connections.ClientErrorHandler;
import Information.Action;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

public class RequestDirectory extends SwingWorker<Void, Void> {
    private final FileManagerGUI fileManagerGUI;
    private String directory;
    private boolean goBackward;
    private boolean goForward;

    public RequestDirectory(FileManagerGUI fileManagerGUI, Movement movement) {
        checkForMovement(movement);
        this.fileManagerGUI = fileManagerGUI;
    }

    public RequestDirectory(FileManagerGUI fileManagerGUI, String directory, Movement movement) {
        checkForMovement(movement);
        this.fileManagerGUI = fileManagerGUI;
        this.directory = directory;
    }

    private void checkForMovement(Movement movement) {
        switch (movement) {
            case FORWARD -> goForward = true;
            case BACKWARD -> goBackward = true;
        }
    }

    private List<String> list;
    private int divider;
    private String path;

    @Override
    protected Void doInBackground() {
        Stack<String> stack = fileManagerGUI.getStack();
        if (goBackward) {
            stack.pop();
            path = stack.peek();
        } else if (goForward) {
            path = stack.isEmpty() ? directory : stack.peek() + directory + "\\";
            stack.push(path);
        } else {
            path = stack.peek();
        }
        try {
            list = fileManagerGUI.getStream().sendAndReadAction(Action.R_A_DIR, path);
        } catch (IOException e) {
            new ClientErrorHandler("Unable to read directory, connection lost with client",
                    fileManagerGUI.getFileManagerDialog(), fileManagerGUI.getStream().getClientSocket());
        }
        divider = list.indexOf("/");
        list.remove(divider);
        return null;
    }

    @Override
    protected void done() {
        if (list != null) {
            System.out.println();
            if (!list.isEmpty() && list.get(0).equals("ACCESS_DENIED")) {
                JOptionPane.showMessageDialog(null, "Access denied to this folder",
                        "Access denied", JOptionPane.ERROR_MESSAGE);
                fileManagerGUI.getStack().pop();
            } else {
                fileManagerGUI.setDivider(divider);
                fileManagerGUI.getPathField().setText(path);
                DefaultTableModel tableModel = (DefaultTableModel) fileManagerGUI.getTable().getModel();
                tableModel.setRowCount(0);
                tableModel.addRow(new String[]{"..."});
                for (int i = 0; i < list.size(); i++) {
                    if (i >= divider) {
                        tableModel.addRow(new String[]{list.get(i), list.get(i + 1)});
                        i++;
                    } else tableModel.addRow(new String[]{list.get(i), ""});

                }
                fileManagerGUI.getScrollPane().getVerticalScrollBar().setValue(0);
            }
        } else {
            new ClientErrorHandler("Unable to enter directory, connection lost with client",
                    fileManagerGUI.getFileManagerDialog(),
                    fileManagerGUI.getStream().getClientSocket());
        }
    }


}


