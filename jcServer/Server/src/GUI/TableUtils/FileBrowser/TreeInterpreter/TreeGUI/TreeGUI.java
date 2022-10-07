package GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI;

import Connections.Streams;
import GUI.TableUtils.FileBrowser.TreeInterpreter.DirectoryTreeGUI.DirectoryFolderRequest;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI.Menus.*;
import GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils.Action;
import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class TreeGUI {


    private final JDialog dialog;
    private final JTree tree;
    private final Streams stream;
    private List<String> filesArray;
    private Action action;
    private final DefaultMutableTreeNode rootNode;


    public TreeGUI(String rootName, Streams stream, Window frame) {
        this.rootNode = new DefaultMutableTreeNode(rootName);
        this.stream = stream;
        this.tree = new JTree(rootNode);
        this.rootNode.add(new DefaultMutableTreeNode("<LOADING DIRECTORY>"));
        this.tree.addTreeWillExpandListener(new FolderRequest(tree, stream));
        this.dialog = new JDialog(frame, "Tree Directory - " + stream.getIdentifier());
        loadStyle();
        addFrame();
        addComponents();
        startDialog();

    }

    public TreeGUI(String rootName, Streams stream, Window frame, List<String> filesArray, Action action) {
        this.rootNode = new DefaultMutableTreeNode(rootName);
        this.stream = stream;
        this.tree = new JTree(rootNode);
        this.rootNode.add(new DefaultMutableTreeNode("<LOADING DIRECTORY>"));
        this.tree.addTreeWillExpandListener(new DirectoryFolderRequest(tree, stream));
        this.dialog = new JDialog(frame, action + " Directory - " + stream.getIdentifier());
        this.filesArray = new ArrayList<>(filesArray);
        this.action = action;
        loadStyle();
        addFrame();
        addComponents();
        startDialog();
    }


    public void startDialog() {
        tree.expandRow(0);
        dialog.setVisible(true);
    }

    private void loadStyle() {
        FlatDarculaLaf.setup();
    }

    private void addFrame() {
        dialog.setSize(new Dimension(400, 300));
        dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
    }


    private void addComponents() {
        tree.setShowsRootHandles(true);
        createMenuBar();
        createPopUpMenu();
        JScrollPane scrollPane = new JScrollPane(tree);
        dialog.add(scrollPane);

    }

    protected void createPopUpMenu() {

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem downloadItem = new JMenuItem("Download");
        JMenu copyMenu = new JMenu("Copy to");
        JMenu moveMenu = new JMenu("Move to");
        JMenuItem uploadMenu = new JMenuItem("Upload");
        JMenuItem runItem = new JMenuItem("Run");
        JMenuItem deleteItem = new JMenuItem("Delete");

        popupMenu.add(downloadItem);
        popupMenu.add(copyMenu);
        popupMenu.add(moveMenu);
        popupMenu.add(uploadMenu);
        popupMenu.add(deleteItem);
        popupMenu.add(runItem);


        downloadItem.addActionListener(new DownloadAction(tree, stream));
        copyMenu.addMenuListener(new CopyMenu(tree, stream, dialog, copyMenu));
        moveMenu.addMenuListener(new MoveMenu(tree, stream, dialog, moveMenu));
        uploadMenu.addActionListener(new UploadAction(tree, stream, dialog));
        runItem.addActionListener(new RunAction(tree, stream));
        deleteItem.addActionListener(new DeleteAction(tree, stream));
        tree.addMouseListener(new TreeListener(tree, popupMenu));
    }

    private void createMenuBar() {
        JMenuBar menu_bar = new JMenuBar();
        JMenu menu = new JMenu("size");
        JMenuItem size = new JMenuItem("size");
        menu.add(size);
        menu_bar.add(menu);
        dialog.setJMenuBar(menu_bar);
    }


    public JTree getTree() {
        return tree;
    }

    public Streams getStream() {
        return stream;
    }

    public List<String> getFilesArray() {
        return filesArray;
    }

    public JDialog getDialog() {
        return dialog;
    }

    public Action getAction() {
        return action;
    }


}