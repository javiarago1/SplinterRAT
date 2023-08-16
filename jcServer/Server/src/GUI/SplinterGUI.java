package GUI;


import Connections.Server;
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
import java.net.Socket;
import java.util.Objects;


public class SplinterGUI {

    private final String[] column = {"IP", "Country", "Tag", "Username", "Operating System", "Status"};
    private JPanel mainPanel;
    private JTable connectionsTable;
    private JFrame mainGUI;
    private final JLabel listeningPort = new JLabel();

    private final JLabel clientsConnected = new JLabel();
    private final GridBagConstraints gridBagConstraints = new GridBagConstraints();

    public SplinterGUI() {
        loadStyle();
        setUpFrame();
        addComps();
    }


    private void setUpFrame() {
        mainGUI = new JFrame("SplinterRAT Interface");
        mainGUI.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("splinter_icon_250x250.png"))).getImage());
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

        JMenu aboutMenu = new JMenu("About");
        aboutMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("splinter_icon_250x250.png")));
                Image image = imageIcon.getImage(); // transform it
                Image scaledImage = image.getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
                imageIcon = new ImageIcon(scaledImage);  // transform it back
                String aboutMessage = "<html>" +
                        "<h3>Splinter RAT</h3>" +
                        "<p>Splinter RAT is a remote administration tool for Windows machines. <br> <br> Developed under the <strong>MIT license</strong> </p><br>" +
                        "• You're responsible for everything you do with SplinterRAT. <br>" +
                        "• This program can only be installed on machines with legal permission. <br>" +
                        "• This program can only be used for ethical hacking purposes.<br>" +
                        "</html>";
                JOptionPane.showMessageDialog(mainGUI, aboutMessage, "About SplinterRAT",
                        JOptionPane.INFORMATION_MESSAGE,
                        new ImageIcon(imageIcon.getImage()));

            }
        });
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(aboutMenu);

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
        connectionsTable.getTableHeader().setReorderingAllowed(false);
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
        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        Color color = new Color(135, 135, 135);
        listeningPort.setForeground(color);
        listeningPort.setFont(font);
        EmptyBorder emptyBorder = new EmptyBorder(1, 6, 1, 8);
        listeningPort.setBorder(emptyBorder);
        bottomInformationPanel.add(listeningPort, BorderLayout.LINE_END);

        updateNumOfConnectedClients();
        clientsConnected.setBorder(emptyBorder);
        clientsConnected.setFont(font);
        clientsConnected.setForeground(color);
        bottomInformationPanel.add(clientsConnected, BorderLayout.LINE_START);
        mainPanel.add(bottomInformationPanel, gridBagConstraints);


    }

    public void updateNumOfConnectedClients() {
        clientsConnected.setText("Connected: " + Main.server.getNumOfConnectedDevices());
        //for (Socket key : Main.server.getMap().keySet()) {
        //    System.out.println(key);
        //}
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

    public JLabel getClientsConnected() {
        return clientsConnected;
    }
}