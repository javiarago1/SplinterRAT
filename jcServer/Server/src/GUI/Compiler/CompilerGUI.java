package GUI.Compiler;

import javax.swing.*;

import java.awt.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class CompilerGUI {
    private JTabbedPane tabPane;
    private final JDialog compilerDialog;
    private final GridBagConstraints constraints = new GridBagConstraints();
    private JButton compileButton;
    private final Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

    private JCheckBox webcamCheckBox;

    public CompilerGUI(JFrame parentFrame) {
        compilerDialog = new JDialog(parentFrame, "Compiler");
        compilerDialog.setModal(true);
        compilerDialog.setSize(450, 275);
        compilerDialog.setLocationRelativeTo(null);
        compilerDialog.setLayout(new GridBagLayout());
        addTabbedPane();
        addLowerPanel();
        compilerDialog.setVisible(true);
    }

    private void addTabbedPane() {
        tabPane = new JTabbedPane();
        addIdentificationPanel();
        addInstallationPanel();
        addMonitorPanel();
        addAssemblyPanel();
        addCompilePanel();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        compilerDialog.add(tabPane, constraints);
    }

    private void addLowerPanel() {
        JPanel lowerPanel = new JPanel();
        lowerPanel.setBackground(new Color(57, 60, 62));
        lowerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton goBackButton = new JButton("< Back ");
        goBackButton.setVisible(false);
        goBackButton.setBackground(new Color(26, 33, 42));
        goBackButton.addActionListener(e -> tabPane.setSelectedIndex(tabPane.getSelectedIndex() - 1));
        lowerPanel.add(goBackButton);

        lowerPanel.add(compileButton);

        JButton nextButton = new JButton("Next >");
        nextButton.setCursor(handCursor);
        nextButton.setBackground(new Color(23, 32, 42));
        nextButton.addActionListener(e -> tabPane.setSelectedIndex(tabPane.getSelectedIndex() + 1));
        lowerPanel.add(nextButton);
        tabPane.addChangeListener(e -> {
            switch (tabPane.getSelectedIndex()) {
                case 0 -> {
                    compileButton.setVisible(false);
                    goBackButton.setVisible(false);
                    nextButton.setVisible(true);
                }
                case 1, 2, 3 -> {
                    goBackButton.setVisible(true);
                    nextButton.setVisible(true);
                    compileButton.setVisible(false);
                }
                case 4 -> {
                    nextButton.setVisible(false);
                    compileButton.setVisible(true);
                    goBackButton.setVisible(true);
                }
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weighty = 0.0;
        compilerDialog.add(lowerPanel, constraints);
    }

    private void addCompilePanel() {
        JPanel compilePanel = new JPanel();
        compilePanel.setLayout(null);


        JLabel tagLabel = new JLabel("Path where the compiler (g++) is located if it isn't in the system variables:");
        tagLabel.setBounds(10, 10, 400, 20);
        compilePanel.add(tagLabel);
        String defaultCompiler = "Default system compiler";
        JComboBox<String> compilerComboBox = new JComboBox<>();
        compilerComboBox.addItem(defaultCompiler);
        compilerComboBox.addItem("Select custom path");

        JTextField compilerPathField = new JTextField("g++");
        compilerPathField.setEditable(false);
        compilerPathField.setBounds(190, 40, 125, 25);
        compilePanel.add(compilerPathField);
        compilerComboBox.addActionListener(e -> {
            String selectedItem = (String) compilerComboBox.getSelectedItem();
            compileButton.setEnabled(false);
            if (selectedItem != null && !selectedItem.equals(defaultCompiler)) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fc.showSaveDialog(compilerDialog);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFolder = fc.getSelectedFile();
                    compilerPathField.setText(selectedFolder.toString() + "\\g++");
                } else {
                    compilerComboBox.setSelectedIndex(0);
                }
            } else {
                compilerPathField.setText("g++");
            }

        });
        compilerComboBox.setBounds(10, 40, 175, 25);
        compilePanel.add(compilerComboBox);

        compileButton = new JButton("Compile");
        compileButton.setVisible(false);
        compileButton.setCursor(handCursor);
        compileButton.setBackground(new Color(0, 136, 6));
        compileButton.addActionListener(new Compiler(compilerDialog,
                new JCheckBox[]{installCheckBox, persistentClientCheckBox,webcamCheckBox},
                new JTextField[]{ipField, portField, tagField, mutexField, timingField, compilerPathField,subdirectoryNameField,executableNameField,clientNameStartUp},radioGroup));
        compileButton.setToolTipText("You must check the version you have " +
                "installed on your system using the g++ check button");
        compileButton.setEnabled(false);


        JButton checkButton = new JButton("Check g++");
        checkButton.setBounds(320, 40, 90, 25);
        checkButton.addActionListener(new VersionChecker(compilerPathField, compilerDialog, compileButton));
        compilePanel.add(checkButton);

        tabPane.add(compilePanel, "Compiler");
    }


    private void addAssemblyPanel() {
        JPanel assemblyPanel = new JPanel();
        assemblyPanel.setLayout(null);

        JLabel tagLabel = new JLabel("Assembly spec:");
        tagLabel.setBounds(10, 10, 210, 20);
        assemblyPanel.add(tagLabel);
        tabPane.add(assemblyPanel, "Assembly");

    }


    private void addMonitorPanel() {
        JPanel monitorPanel = new JPanel();
        monitorPanel.setLayout(null);
        webcamCheckBox = new JCheckBox("Enable webcam monitoring");
        webcamCheckBox.setBounds(10, 10, 210, 20);
        monitorPanel.add(webcamCheckBox);

        JCheckBox keyloggerCheckBox = new JCheckBox("Enable keylogger monitoring");
        keyloggerCheckBox.setBounds(10, 40, 210, 20);
        monitorPanel.add(keyloggerCheckBox);


        JSeparator lastHorizontalSeparator = new JSeparator();
        lastHorizontalSeparator.setBounds(0, 170, 400, 5);
        monitorPanel.add(lastHorizontalSeparator);

        tabPane.add(monitorPanel, "Monitoring");
    }

    private JCheckBox installCheckBox;

    private JCheckBox persistentClientCheckBox;

    private JTextField subdirectoryNameField;

    private JTextField executableNameField;

    private ButtonGroup radioGroup;

    private JTextField clientNameStartUp;

    private void addInstallationPanel() {
        JPanel installationPanel = new JPanel();
        installationPanel.setLayout(null);

        installCheckBox = new JCheckBox("Install client on computer");
        installCheckBox.setBounds(10, 10, 210, 20);
        installationPanel.add(installCheckBox);

        JSeparator verticalSeparator = new JSeparator(SwingConstants.VERTICAL);
        verticalSeparator.setBounds(200, 10, 5, 110);
        installationPanel.add(verticalSeparator);

        JLabel subdirectoryNameLabel = new JLabel("Subdirectory name: ");
        subdirectoryNameLabel.setBounds(220, 10, 210, 20);
        installationPanel.add(subdirectoryNameLabel);

        subdirectoryNameField = new JTextField("Client");
        subdirectoryNameField.addFocusListener(new FieldListener(subdirectoryNameField, "Client"));
        subdirectoryNameField.setBounds(330, 10, 85, 20);
        installationPanel.add(subdirectoryNameField);

        JLabel executableNameLabel = new JLabel("Executable name: ");
        executableNameLabel.setBounds(220, 40, 210, 20);
        installationPanel.add(executableNameLabel);

        executableNameField = new JTextField("client");
        executableNameField.addFocusListener(new FieldListener(executableNameField, "client"));
        executableNameField.setBounds(330, 40, 85, 20);
        installationPanel.add(executableNameField);

        JRadioButton installOnProgramFiles = new JRadioButton("Install on program files (x86)");
        installOnProgramFiles.setActionCommand("0");
        installOnProgramFiles.setBounds(10, 40, 210, 20);
        installationPanel.add(installOnProgramFiles);

        JRadioButton installOnSystem = new JRadioButton("Install on system directory");
        installOnSystem.setActionCommand("1");
        installOnSystem.setBounds(10, 70, 210, 20);
        installationPanel.add(installOnSystem);

        JRadioButton installOnAppData = new JRadioButton("Install on AppData", true);
        installOnAppData.setActionCommand("2");
        installOnAppData.setBounds(10, 100, 210, 20);
        installationPanel.add(installOnAppData);

        radioGroup = new ButtonGroup();
        radioGroup.add(installOnProgramFiles);
        radioGroup.add(installOnSystem);
        radioGroup.add(installOnAppData);

        JSeparator horizontalSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalSeparator.setBounds(0, 130, 450, 5);
        installationPanel.add(horizontalSeparator);

        persistentClientCheckBox = new JCheckBox("Client start on start up");
        persistentClientCheckBox.setBounds(10, 140, 210, 20);
        installationPanel.add(persistentClientCheckBox);

        JLabel clientNameStartUPLabel = new JLabel("Name for start up file: ");
        clientNameStartUPLabel.setBounds(180, 140, 150, 20);
        installationPanel.add(clientNameStartUPLabel);

        JSeparator verticalSeparatorStartUp = new JSeparator(SwingConstants.VERTICAL);
        verticalSeparatorStartUp.setBounds(163, 136, 5, 30);
        installationPanel.add(verticalSeparatorStartUp);

        clientNameStartUp = new JTextField("ClientStartUp");
        clientNameStartUp.addFocusListener(new FieldListener(clientNameStartUp, "ClientStartUp"));
        clientNameStartUp.setBounds(300, 141, 100, 20);
        installationPanel.add(clientNameStartUp);

        List<JComponent> listOfComponentsInstallation = new ArrayList<>();
        listOfComponentsInstallation.add(installOnProgramFiles);
        listOfComponentsInstallation.add(installOnAppData);
        listOfComponentsInstallation.add(installOnSystem);
        listOfComponentsInstallation.add(subdirectoryNameLabel);
        listOfComponentsInstallation.add(subdirectoryNameField);
        listOfComponentsInstallation.add(executableNameLabel);
        listOfComponentsInstallation.add(executableNameField);
        listOfComponentsInstallation.add(persistentClientCheckBox);
        listOfComponentsInstallation.add(clientNameStartUp);
        listOfComponentsInstallation.add(clientNameStartUPLabel);

        changeStateOfElements(listOfComponentsInstallation, false);

        List<JComponent> listOfStartUPComps = new ArrayList<>();
        listOfStartUPComps.add(clientNameStartUp);
        listOfStartUPComps.add(clientNameStartUPLabel);

        installCheckBox.addActionListener(e -> changeStateOfElements(listOfComponentsInstallation, ((AbstractButton) e.getSource()).getModel().isSelected()));
        persistentClientCheckBox.addActionListener(e -> changeStateOfElements(listOfStartUPComps, ((AbstractButton) e.getSource()).getModel().isSelected()));

        tabPane.add(installationPanel, "Installation");

    }

    private void changeStateOfElements(List<JComponent> listOfComponents, boolean isEnabled) {
        for (int i=0;i<listOfComponents.size();i++) {
            if (i==7 && !((JCheckBox)listOfComponents.get(i)).isSelected() && isEnabled){
                listOfComponents.get(i).setEnabled(true);
                isEnabled= false;
            } else {
                listOfComponents.get(i).setEnabled(isEnabled);
            }

        }
    }


    private JTextField ipField;
    private JTextField portField;
    private JTextField mutexField;
    private JTextField tagField;
    private JTextField timingField;

    private void addIdentificationPanel() {

        JPanel identificationPanel = new JPanel();
        identificationPanel.setLayout(null);


        JLabel tagLabel = new JLabel("Client tag for connection identification:");
        tagLabel.setBounds(10, 10, 210, 20);
        identificationPanel.add(tagLabel);
        tagField = new JTextField("Client");
        tagField.setBounds(225, 11, 190, 20);
        tagField.addFocusListener(new FieldListener(tagField, "Client"));
        identificationPanel.add(tagField);

        JSeparator horizontal = new JSeparator();
        horizontal.setBounds(5, 100, 425, 5);
        horizontal.setOrientation(SwingConstants.HORIZONTAL);
        identificationPanel.add(horizontal);

        JLabel mutexLabel = new JLabel("Mutex is used to avoid executing the same client multiple times.");
        mutexLabel.setBounds(10, 40, 400, 20);
        identificationPanel.add(mutexLabel);
        mutexField = new JTextField(Mutex.generateMutex());
        mutexField.setBounds(10, 70, 250, 20);
        identificationPanel.add(mutexField);

        JButton mutexButton = new JButton("Generate mutex");
        mutexButton.setBounds(270, 70, 145, 20);
        mutexButton.addActionListener(e -> mutexField.setText(Mutex.generateMutex()));
        identificationPanel.add(mutexButton);

        JLabel ipLabel = new JLabel("IP/Hostname:");
        ipLabel.setBounds(10, 110, 100, 20);
        identificationPanel.add(ipLabel);
        String defaultIP = "192.168.1.133";
        ipField = new JTextField(defaultIP);
        ipField.setBounds(90, 110, 100, 20);
        ipField.addFocusListener(new FieldListener(ipField, defaultIP));
        identificationPanel.add(ipField);


        JLabel portLabel = new JLabel("PORT:");
        portLabel.setBounds(50, 140, 50, 20);
        identificationPanel.add(portLabel);
        String defaultPort = "3055";
        portField = new JTextField(defaultPort);
        portField.setBounds(90, 140, 50, 20);
        portField.addFocusListener(new FieldListener(portField, defaultPort));
        identificationPanel.add(portField);

        JSeparator vertical = new JSeparator();
        vertical.setBounds(200, 106, 10, 60);
        vertical.setOrientation(SwingConstants.VERTICAL);
        identificationPanel.add(vertical);


        JLabel timingLabel = new JLabel("Timeout to retry connection:");
        timingLabel.setBounds(210, 110, 250, 20);
        identificationPanel.add(timingLabel);
        String defaultTime = "10000";
        timingField = new JTextField(defaultTime);
        timingField.setBounds(212, 140, 80, 20);
        timingField.addFocusListener(new FieldListener(timingField, defaultTime));
        identificationPanel.add(timingField);
        JLabel uniteLabel = new JLabel("ms");
        uniteLabel.setBounds(295, 140, 30, 20);
        identificationPanel.add(uniteLabel);


        tabPane.addTab("Identification", identificationPanel);
    }
}
