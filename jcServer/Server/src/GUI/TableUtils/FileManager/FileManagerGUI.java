package GUI.TableUtils.FileManager;


import Connections.Streams;
import GUI.TableUtils.FileManager.Actions.DownloadAction;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Stack;

public class FileManagerGUI {


    private final Stack<String> stack = new Stack<>();
    private final Streams stream;

    private final JComboBox<String> diskComboBox;

    private final JTable table;

    private final JTextField textField;

    private final JScrollPane scrollPane;

    private final JDialog fileManagerDialog;

    public FileManagerGUI(Streams stream, JFrame mainGUI) {
        this.stream = stream;
        fileManagerDialog = new JDialog(mainGUI, "File Manager - " + stream.getIdentifier());
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
        diskComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object item = e.getItem();
                stack.clear();
                requestDirectory((String) item);
            }
        });


        table = new JTable(new TableModel());
        TableColumn someColumn = table.getColumnModel().getColumn(0);
        someColumn.setCellRenderer(new CellRenderer(this));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(2).setMinWidth(100);
        table.getColumnModel().getColumn(2).setMaxWidth(220);
        table.getColumnModel().getColumn(1).setMinWidth(100);
        table.getColumnModel().getColumn(1).setMaxWidth(200);


        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;


        scrollPane = new JScrollPane(table);
        fileManagerDialog.add(scrollPane, constraints);

        //add disks

        requestDisk();

        createPopUpMenu();

        table.addMouseListener(new MouseListener(this));

        fileManagerDialog.setLocationRelativeTo(null);
        fileManagerDialog.setVisible(true);

    }

    private void createPopUpMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem downloadItem = new JMenuItem("Download");
        JMenuItem copyMenu = new JMenuItem("Copy");
        JMenuItem moveMenu = new JMenuItem("Move");
        JMenuItem uploadMenu = new JMenuItem("Upload");
        JMenuItem runItem = new JMenuItem("Run");
        JMenuItem deleteItem = new JMenuItem("Delete");

        popupMenu.add(downloadItem);
        popupMenu.add(copyMenu);
        popupMenu.add(moveMenu);
        popupMenu.add(uploadMenu);
        popupMenu.add(deleteItem);
        popupMenu.add(runItem);

        downloadItem.addActionListener(new DownloadAction(this));


        table.setComponentPopupMenu(popupMenu);


    }


    private void requestDisk() {
        stream.getExecutor().submit(new RequestDisk(this));
    }

    private int divider;

    protected void requestDirectory(String directory) {
        stream.getExecutor().submit(new RequestDirectory(this, directory));
    }

    protected void requestDirectory() {
        stream.getExecutor().submit(new RequestDirectory(this));
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

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JDialog getFileManagerDialog() {
        return fileManagerDialog;
    }
}
