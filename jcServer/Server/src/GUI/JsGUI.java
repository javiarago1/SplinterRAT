package GUI;

import Connections.Server;
import Connections.Streams;

import GUI.Compiler.CompilerGUI;
import GUI.TableUtils.Configuration.StateColumnRenderer;
import GUI.TableUtils.Configuration.TableModel;
import GUI.TableUtils.Configuration.TablePopUpListener;
import com.formdev.flatlaf.FlatDarkLaf;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


public class JsGUI {

    private final String[] column = {"IP", "Country", "Tag", "Username", "Operating System", "Status"};
    private JPanel panel;
    private JTable connectionsTable;
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
        // Panel
        addJPanel();
        //
        // JTable
        setupTable();
        //

        mainGUI.setVisible(true);
    }







    private void addTable() {
        TableModel tableModel = new TableModel(column);
        connectionsTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(connectionsTable);
        connectionsTable.setFocusable(false);
        tableScroll.setBounds(0, 0, 784, 780);
        connectionsTable.addMouseListener(new TablePopUpListener(this));

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

        for (int x = 0; x < connectionsTable.getColumnCount(); x++) {
            if (x == connectionsTable.getColumnCount() - 1) {
                connectionsTable.getColumnModel().getColumn(x).setCellRenderer(columnRenderer);
            } else {
                connectionsTable.getColumnModel().getColumn(x).setCellRenderer(alignRenderer);
            }
        }

    }

    public ConcurrentHashMap<Socket, Streams> getMap() {
        return map;
    }

    public JFrame getMainGUI() {
        return mainGUI;
    }

    public JTable getConnectionsTable() {
        return connectionsTable;
    }

    public DefaultTableModel getConnectionsDefaultTableModel() {
        return (DefaultTableModel) connectionsTable.getModel();
    }


}