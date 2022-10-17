package GUI;

import Connections.Server;
import Connections.Streams;

import GUI.Compiler.CompilerGUI;
import GUI.TableUtils.Configuration.StateColumnRenderer;
import GUI.TableUtils.Configuration.TableMenuListener;
import GUI.TableUtils.Configuration.TableModel;
import GUI.TableUtils.FileManager.Listener.FileManagerMenuListener;
import GUI.TableUtils.Webcam.WebcamMenuListener;
import com.formdev.flatlaf.FlatDarkLaf;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


public class JsGUI {

    private final String[] column = {"IP", "Country", "Tag", "Username", "Operating System", "Status"};
    private JPanel panel;
    private JTable connectionTable;
    private JFrame mainGUI;

    private final ConcurrentHashMap<Socket, Streams> map;

    public JsGUI(Server server) {
        map = server.getMap();
        loadStyle();
        setUpFrame();
        addComps();
    }


    private void setUpFrame() {
        mainGUI = new JFrame("jRat Interface");
        mainGUI.setSize(new Dimension(800, 400));
        mainGUI.setResizable(false);
        mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGUI.setLocationRelativeTo(null);

    }

    private void loadStyle() {
        FlatDarkLaf.setup();
        UIManager.put("Component.focusedBorderColor", new Color(55, 55, 55));
    }


    private void addJMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu builderMenu = new JMenu("Builder");
        builderMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new CompilerGUI(mainGUI);
            }
        });
        menuBar.add(builderMenu);
        mainGUI.setJMenuBar(menuBar);
    }

    private void addJPanel(){
        panel = new JPanel();
        panel.setLayout(null);
    }

    private void addComps() {
        // Menu
        addJMenu();
        //
        // PopUp Menu
        addPopUpMenu();
        //
        // Panel
        addJPanel();
        //
        // JTable
        setupTable();
        //


        mainGUI.setVisible(true);
    }


    // Menus of table
    private JPopupMenu popupMenu;
    private JMenuItem fileManagerMenu;
    private JMenuItem webcamMenu;


    private void addPopUpMenu() {
        popupMenu = new JPopupMenu();
        fileManagerMenu = new JMenuItem("File Manager");
        webcamMenu = new JMenuItem("Webcam manager");

        // set actions


        popupMenu.add(fileManagerMenu);
        // add to popup

        popupMenu.add(webcamMenu);

    }

    private void addTable() {
        TableModel tableModel = new TableModel(null, column);
        connectionTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(connectionTable);
        connectionTable.setFocusable(false);
        tableScroll.setBounds(0, 0, 784, 780);
        connectionTable.setComponentPopupMenu(popupMenu);


        connectionTable.addMouseListener(new TableMenuListener(connectionTable));
        webcamMenu.addActionListener(new WebcamMenuListener(connectionTable, map, this));
        fileManagerMenu.addActionListener(new FileManagerMenuListener(connectionTable, map, this));


        panel.add(tableScroll);
        mainGUI.add(panel);
    }

    private void setupTable() {
        addTable();
        styleTable();
    }

    private void styleTable() {
        StateColumnRenderer columnRenderer = new StateColumnRenderer();

        DefaultTableCellRenderer alignRenderer = new DefaultTableCellRenderer();
        alignRenderer.setHorizontalAlignment(JLabel.CENTER);

        for (int x = 0; x < connectionTable.getColumnCount(); x++) {
            if (x == connectionTable.getColumnCount() - 1) {
                connectionTable.getColumnModel().getColumn(x).setCellRenderer(columnRenderer);
            } else {
                connectionTable.getColumnModel().getColumn(x).setCellRenderer(alignRenderer);
            }
        }

    }


    public JFrame getMainGUI() {
        return mainGUI;
    }

    public JTable getConnectionTable() {
        return connectionTable;
    }



}