package GUI.TableUtils.FileManager;


import Connections.Streams;
import GUI.TableUtils.FileManager.Actions.*;
import GUI.TableUtils.FileManager.Listener.MouseListener;
import GUI.TableUtils.FileManager.Style.CellRenderer;
import GUI.TableUtils.FileManager.Style.TableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FileManagerGUI {


    private final Stack<String> stack = new Stack<>();
    private final Streams stream;

    private final JComboBox<String> diskComboBox;

    private final JTable table;

    private final JTextField textField;

    private final JScrollPane scrollPane;

    private final JDialog fileManagerDialog;

    private JPopupMenu popupMenu;

    public FileManagerGUI(Streams stream, JFrame mainGUI) {
        if (stream == null) throw new IllegalStateException("Problems");
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
        table.getColumnModel().getColumn(0).setMinWidth(400);
        table.getColumnModel().getColumn(0).setMaxWidth(500);
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


    private boolean copySelected;

    private boolean cutSelected;

    public boolean isCopySelected() {
        return copySelected;
    }

    public boolean isCutSelected() {
        return cutSelected;
    }

    public void setCopySelected(boolean copySelected) {
        this.copySelected = copySelected;
    }

    public void setCutSelected(boolean cutSelected) {
        this.cutSelected = cutSelected;
    }

    List<String> CMElements = new ArrayList<>();


    public List<String> getCMElements() {
        return CMElements;
    }

    public void setCMElements(List<String> CMElements) {
        this.CMElements = CMElements;
    }

    private void createPopUpMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem downloadItem = new JMenuItem("Download");
        JMenuItem copyMenu = new JMenuItem("Copy");
        JMenuItem cutMenu = new JMenuItem("Cut");
        JMenuItem pasteMenu = new JMenuItem("Paste");
        JMenuItem uploadMenu = new JMenuItem("Upload");
        JMenuItem runItem = new JMenuItem("Run");
        JMenuItem deleteItem = new JMenuItem("Delete");

        popupMenu.add(downloadItem);
        popupMenu.add(copyMenu);
        popupMenu.add(cutMenu);
        popupMenu.add(pasteMenu);
        popupMenu.add(uploadMenu);
        popupMenu.add(deleteItem);
        popupMenu.add(runItem);


        downloadItem.addActionListener(new DownloadAction(this));
        copyMenu.addActionListener(new CopyAction(this));
        cutMenu.addActionListener(new CutAction(this));
        pasteMenu.addActionListener(new PasteAction(this));
        uploadMenu.addActionListener(new UploadAction(this));
        deleteItem.addActionListener(new DeleteAction(this));
        runItem.addActionListener(new RunAction(this));


    }


    private void requestDisk() {
        stream.getExecutor().submit(new RequestDisk(this));
    }

    private int divider;

    public void requestDirectory(String directory) {
        stream.getExecutor().submit(new RequestDirectory(this, directory));
    }

    public void requestDirectory() {
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

    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

}
