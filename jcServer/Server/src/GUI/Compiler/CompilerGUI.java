package GUI.Compiler;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class CompilerGUI {
    public CompilerGUI() {
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setSize(400, 275);
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);

        JTabbedPane tabPane = new JTabbedPane();

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


        JButton nextButton = new JButton("Next >");
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.setBackground(new Color(23, 32, 42));
        nextButton.setBounds(298, 178, 80, 20);
        nextButton.addActionListener(e -> tabPane.setSelectedIndex(1));
        identificationPanel.add(nextButton);

        JSeparator lastHorizontalSeparator = new JSeparator();
        lastHorizontalSeparator.setBounds(0, 170, 400, 5);
        identificationPanel.add(lastHorizontalSeparator);


        JLabel timingLabel = new JLabel("Timeout to retry connection:");
        timingLabel.setBounds(210, 110, 250, 20);
        identificationPanel.add(timingLabel);
        JTextField timingField = new JTextField("10000");
        timingField.setBounds(212, 140, 80, 20);
        identificationPanel.add(timingField);
        JLabel uniteLabel = new JLabel("ms");
        uniteLabel.setBounds(295, 140, 30, 20);
        identificationPanel.add(uniteLabel);


        // add panel to tab
        tabPane.addTab("Client identification", identificationPanel);

        JPanel panel2 = new JPanel();
        tabPane.addTab("Panel 2", panel2);

        //Componentes del panel2
        JLabel et_p2 = new JLabel("Estas en el panel 2");
        panel2.add(et_p2);

        JPanel panel3 = new JPanel();

        //Componentes del panel3
        JLabel et_p3 = new JLabel("Estas en el panel 3");
        panel3.add(et_p3);

        tabPane.addTab("Panel 3", panel3);

        JPanel panel4 = new JPanel();

        //Componentes del panel4
        JLabel et_p4 = new JLabel("Estas en el panel 4");
        panel4.add(et_p4);

        tabPane.addTab("Panel 4", panel4);
        dialog.add(tabPane);


        dialog.setVisible(true);


    }


    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new FlatDarkLaf());
        new CompilerGUI();
    }
}
