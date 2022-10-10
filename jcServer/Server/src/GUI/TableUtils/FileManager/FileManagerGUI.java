package GUI.TableUtils.FileManager;


import Connections.Streams;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.Stack;

public class FileManagerGUI {


    private final Stack<String> stack = new Stack<>();
    private final Streams stream;

    private final JComboBox<String> diskComboBox;

    private final JTable table;

    private final JTextField textField;

    public FileManagerGUI(Streams stream, JFrame mainGUI) {

        this.stream = stream;
        JDialog fileManagerDialog = new JDialog(mainGUI, "File Manager - " + stream.getIdentifier());
        fileManagerDialog.setSize(new Dimension(600, 340));

        fileManagerDialog.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        JButton button = new JButton("↻");
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        fileManagerDialog.add(button, constraints);


        textField = new JTextField();
        textField.setEditable(false);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weighty = 0.0;
        constraints.weightx = 1;
        constraints.insets = new Insets(5, 5, 5, 5);
        fileManagerDialog.add(textField, constraints);


        constraints.fill = GridBagConstraints.HORIZONTAL;
        diskComboBox = new JComboBox<>();
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0.25;
        fileManagerDialog.add(diskComboBox, constraints);


        table = new JTable();
        table.setModel(new TableModel());


        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;


        JScrollPane scrollPane = new JScrollPane(table);
        fileManagerDialog.add(scrollPane, constraints);

        //add disks

        requestDisk();


        table.addMouseListener(new MouseListener(this));

        fileManagerDialog.setLocationRelativeTo(null);
        fileManagerDialog.setVisible(true);

    }


    private void requestDisk() {
        stream.getExecutor().submit(new RequestDisk(this));
    }

    private int divider;

    protected void requestDirectory(String directory, boolean goBack) {
        stream.getExecutor().submit(new RequestDirectory(this, directory, goBack));
    }

    public void setDivider(int divider) {
        this.divider = divider;
    }

    public int getDivider() {
        return divider;
    }

    public Stack<String> getStack() {
        return stack;
    }

    public Streams getStream() {
        return stream;
    }


    public JTextField getTextField() {
        return textField;
    }

    public JComboBox<String> getDiskComboBox() {
        return diskComboBox;
    }

    public JTable getTable() {
        return table;
    }


}
