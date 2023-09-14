package GUI.TableUtils.FileManager;


import Connections.Client;
import Connections.ClientHandler;
import GUI.TableUtils.Configuration.TablePopUpListener;
import GUI.TableUtils.FileManager.Actions.*;
import GUI.TableUtils.FileManager.Event.RequestDirectoryEvent;
import GUI.TableUtils.FileManager.Event.RequestDiskEvent;
import GUI.TableUtils.FileManager.Listener.MouseListener;
import GUI.TableUtils.FileManager.Style.CellRenderer;
import GUI.TableUtils.FileManager.Style.TableModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class FileManagerGUI {


    private final Stack<String> stack = new Stack<>();

    private final JComboBox<String> diskComboBox;

    private final JTable table;

    private final JTextField pathField;

    private final JScrollPane scrollPane;
    private final Client client;


    private final JDialog fileManagerDialog;

    private JPopupMenu popupMenu;

    public void addDisks(List<String> disks) {
        SwingUtilities.invokeLater(() -> {
            if (disks != null) {
                JComboBox<String> diskBox = diskComboBox;
                DefaultComboBoxModel<String> boxModel = (DefaultComboBoxModel<String>) diskBox.getModel();
                for (String e : disks) {
                    if (boxModel.getIndexOf(e) == -1) {
                        diskBox.addItem(e);
                    }
                }
            }
        });
    }


    public FileManagerGUI(Client client, JFrame mainGUI) {
        this.client = client;
        fileManagerDialog = new JDialog(mainGUI, "File Manager -" + client.getIdentifier());
        fileManagerDialog.setSize(new Dimension(600, 340));
        fileManagerDialog.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        JButton refreshCurrentFolder = new JButton("↻");
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        fileManagerDialog.add(refreshCurrentFolder, constraints);

        refreshCurrentFolder.addActionListener(e -> requestDirectory(stack.peek()));


        pathField = new JTextField();
        pathField.setEditable(false);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weighty = 0.0;
        constraints.weightx = 1;
        constraints.insets = new Insets(5, 5, 5, 5);
        fileManagerDialog.add(pathField, constraints);


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
                stack.push((String) item);
                requestDirectory(stack.peek());
            }
        });


        JButton refreshDiskButton = new JButton("↻");
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 0;
        fileManagerDialog.add(refreshDiskButton, constraints);
        refreshDiskButton.addActionListener(e -> requestDisks());

        table = new JTable(new TableModel());
        table.getTableHeader().setReorderingAllowed(false);
        TableColumn someColumn = table.getColumnModel().getColumn(0);
        someColumn.setCellRenderer(new CellRenderer(this));
        table.getColumnModel().getColumn(0).setMinWidth(400);
        table.getColumnModel().getColumn(0).setMaxWidth(500);
        table.getColumnModel().getColumn(1).setMinWidth(100);
        table.getColumnModel().getColumn(1).setMaxWidth(200);


        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;


        scrollPane = new JScrollPane(table);
        fileManagerDialog.add(scrollPane, constraints);

        //add disks



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
        TablePopUpListener.setIconToMenuItem(downloadItem, "download_icon.png");
        JMenuItem copyMenu = new JMenuItem("Copy");
        TablePopUpListener.setIconToMenuItem(copyMenu, "copy_icon.png");
        JMenuItem cutMenu = new JMenuItem("Cut");
        TablePopUpListener.setIconToMenuItem(cutMenu, "cut_icon.png");
        JMenuItem pasteMenu = new JMenuItem("Paste");
        TablePopUpListener.setIconToMenuItem(pasteMenu, "paste_icon.png");
        JMenuItem uploadMenu = new JMenuItem("Upload");
        TablePopUpListener.setIconToMenuItem(uploadMenu, "upload_icon.png");
        JMenuItem runItem = new JMenuItem("Run");
        TablePopUpListener.setIconToMenuItem(runItem, "run_icon.png");
        JMenuItem deleteItem = new JMenuItem("Delete");
        TablePopUpListener.setIconToMenuItem(deleteItem, "delete_icon.png");


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

    public void requestDirectory(String path){
        client.getExecutor().submit(new RequestDirectoryEvent(client, Collections.singletonList(path)));
    }

    public void requestDisks(){
        client.getExecutor().submit(new RequestDiskEvent(client, null));
    }

    private int divider;

    public void setDivider(int divider) {
        this.divider = divider;
    }

    public int getDivider() {
        return divider;
    }

    public Stack<String> getStack() {
        return stack;
    }


    public JTextField getPathField() {
        return pathField;
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

    public Client getClient() {
        return client;
    }
}
