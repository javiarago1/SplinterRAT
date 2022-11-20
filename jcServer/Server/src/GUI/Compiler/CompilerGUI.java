package GUI.Compiler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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

    private JCheckBox keyloggerCheckBox;

    public CompilerGUI(JFrame parentFrame) {
        compilerDialog = new JDialog(parentFrame, "Compiler");
        compilerDialog.setModal(true);
        compilerDialog.setResizable(false);
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
        addModulesPanel();
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
                new JCheckBox[]{installCheckBox, persistentClientCheckBox,webcamCheckBox,keyloggerCheckBox},
                new JTextField[]{ipField, portField, tagField, mutexField, timingField, compilerPathField,
                        subdirectoryNameField,executableNameField,clientNameStartUp,subdirectoryWebcamLogsField,subdirectoryKeyloggerField,
                        fileDescriptionField,fileVersionField,productNameField,copyrightField,originalNameField,iconPathField
                },radioGroup));
        compileButton.setToolTipText("You must check the version you have " +
                "installed on your system using the g++ check button");
        compileButton.setEnabled(false);


        JButton checkButton = new JButton("Check g++");
        checkButton.setBounds(320, 40, 90, 25);
        checkButton.addActionListener(new VersionChecker(compilerPathField, compilerDialog, compileButton));
        compilePanel.add(checkButton);

        tabPane.add(compilePanel, "Compiler");
    }

    private JTextField fileDescriptionField;
    private JTextField fileVersionField;
    private JTextField productNameField;
    private JTextField copyrightField;
    private JTextField originalNameField;

    private JTextField iconPathField;
    private void addAssemblyPanel() {
        JPanel assemblyPanel = new JPanel();
        assemblyPanel.setLayout(null);

        JLabel fileDescriptionLabel = new JLabel("Description of file:");
        fileDescriptionLabel.setBounds(69, 15, 150, 20);
        assemblyPanel.add(fileDescriptionLabel);

        fileDescriptionField = new JTextField("This program is so pretty!");
        fileDescriptionField.setBounds(170, 15, 150, 20);
        assemblyPanel.add(fileDescriptionField);

        JLabel versionOfFileAndProduct = new JLabel("Version of file and product:");
        versionOfFileAndProduct.setBounds(22, 45, 210, 20);
        assemblyPanel.add(versionOfFileAndProduct);

        fileVersionField = new JTextField("0.0.0.0");
        fileVersionField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                fileVersionField.setText(
                        fileVersionField.getText().matches("(\\d)|(\\d\\.\\d)|((\\d\\.){2}\\d)|((\\d\\.){3}\\d)")
                        ? fileVersionField.getText() : "");
            }
        });
        fileVersionField.setBounds(170, 45, 150, 20);
        assemblyPanel.add(fileVersionField);

        JLabel productNameLabel = new JLabel("Name of product:");
        productNameLabel.setBounds(70, 75, 210, 20);
        assemblyPanel.add(productNameLabel);

        productNameField = new JTextField("Splinter client");
        productNameField.setBounds(170, 75, 150, 20);
        assemblyPanel.add(productNameField);


        JLabel copyrightLabel = new JLabel("Copyright:");
        copyrightLabel.setBounds(108, 105, 210, 20);
        assemblyPanel.add(copyrightLabel);

        copyrightField = new JTextField("SplinterRAT ©");
        copyrightField.setBounds(170, 105, 150, 20);
        assemblyPanel.add(copyrightField);

        JLabel originalNameLabel = new JLabel("Original name of file:");
        originalNameLabel.setBounds(54, 135, 210, 20);
        assemblyPanel.add(originalNameLabel);

        originalNameField = new JTextField("client.exe");
        originalNameField.setBounds(170, 135, 150, 20);
        assemblyPanel.add(originalNameField);

        JButton iconButton = new JButton("Select icon");
        iconButton.setBounds(325, 105, 100, 20);
        assemblyPanel.add(iconButton);


        iconPathField = new JTextField();
        iconPathField.setEditable(false);
        iconPathField.setBounds(325, 135, 100, 20);
        assemblyPanel.add(iconPathField);

        iconButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".ico", "ico"));

            int result = fileChooser.showOpenDialog(compilerDialog);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                iconPathField.setText(selectedFile.getAbsolutePath());
            }
        });


        tabPane.add(assemblyPanel, "Assembly");

    }
    private JTextField subdirectoryKeyloggerField;
    private JTextField subdirectoryWebcamLogsField;

    private void addModulesPanel() {
        JPanel monitorPanel = new JPanel();
        monitorPanel.setLayout(null);
        webcamCheckBox = new JCheckBox("Webcam monitoring");
        webcamCheckBox.setBounds(10, 10, 210, 20);
        monitorPanel.add(webcamCheckBox);

        JLabel subdirectoryWebcamLogsLabel = new JLabel("Subdirectory name:");
        subdirectoryWebcamLogsLabel.setBounds(210, 10, 120, 20);
        monitorPanel.add(subdirectoryWebcamLogsLabel);
        subdirectoryWebcamLogsLabel.setEnabled(false);

        JSeparator verticalSeparatorOne = new JSeparator(SwingConstants.VERTICAL);
        verticalSeparatorOne.setBounds(180, 10, 5, 20);
        monitorPanel.add(verticalSeparatorOne);

        subdirectoryWebcamLogsField= new JTextField("WLogs");
        subdirectoryWebcamLogsField.addFocusListener(new FieldListener(subdirectoryWebcamLogsField,"WLogs"));
        subdirectoryWebcamLogsField.setBounds(320, 10, 100, 20);
        monitorPanel.add(subdirectoryWebcamLogsField);
        subdirectoryWebcamLogsField.setEnabled(false);

        JSeparator horizontalSeparatorOne = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalSeparatorOne.setBounds(10, 40, 410, 5);
        monitorPanel.add(horizontalSeparatorOne);

        keyloggerCheckBox = new JCheckBox("Keylogger monitoring");
        keyloggerCheckBox.setBounds(10, 50, 210, 20);
        monitorPanel.add(keyloggerCheckBox);

        JLabel subdirectoryKeyloggerLogsLabel = new JLabel("Subdirectory name:");
        subdirectoryKeyloggerLogsLabel.setBounds(210, 50, 120, 20);
        monitorPanel.add(subdirectoryKeyloggerLogsLabel);
        subdirectoryKeyloggerLogsLabel.setEnabled(false);

        JSeparator verticalSeparatorTwo = new JSeparator(SwingConstants.VERTICAL);
        verticalSeparatorTwo.setBounds(180, 50, 5, 20);
        monitorPanel.add(verticalSeparatorTwo);

        subdirectoryKeyloggerField= new JTextField("KLogs");
        subdirectoryKeyloggerField.addFocusListener(new FieldListener(subdirectoryKeyloggerField,"KLogs"));
        subdirectoryKeyloggerField.setBounds(320, 50, 100, 20);
        monitorPanel.add(subdirectoryKeyloggerField);
        subdirectoryKeyloggerField.setEnabled(false);


        JSeparator horizontalSeparatorTwo = new JSeparator(SwingConstants.HORIZONTAL);
        horizontalSeparatorTwo.setBounds(10, 80, 410, 5);
        monitorPanel.add(horizontalSeparatorTwo);

        List<JComponent>webcamComps = new ArrayList<>();
        webcamComps.add(subdirectoryWebcamLogsLabel);
        webcamComps.add(subdirectoryWebcamLogsField);

        List<JComponent>keyloggerComps = new ArrayList<>();
        keyloggerComps.add(subdirectoryKeyloggerLogsLabel);
        keyloggerComps.add(subdirectoryKeyloggerField);

        webcamCheckBox.addActionListener(e -> webcamComps.forEach((comp)-> comp.setEnabled(((AbstractButton)e.getSource()).getModel().isSelected())));
        keyloggerCheckBox.addActionListener(e -> keyloggerComps.forEach((comp)-> comp.setEnabled(((AbstractButton)e.getSource()).getModel().isSelected())));

        tabPane.add(monitorPanel, "Modules");
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
