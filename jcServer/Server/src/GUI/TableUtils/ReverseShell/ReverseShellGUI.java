package GUI.TableUtils.ReverseShell;

import Connections.Streams;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;

public class ReverseShellGUI {
    private final Streams stream;
    JDialog reverseShellDialog;

    public ReverseShellGUI(Streams stream, JFrame mainGUI) {
        this.stream = stream;
        reverseShellDialog = new JDialog(mainGUI, "Reverse shell " + stream.getIdentifier());
        reverseShellDialog.setSize(new Dimension(500, 300));
        reverseShellDialog.setLocationRelativeTo(null);
        reverseShellDialog.setLayout(new GridBagLayout());
        addStyle();
        reverseShellDialog.setVisible(true);
    }


    JTextField fieldOfCommands;
    JTextArea textAreaOfResult;

    private void addStyle() {
        GridBagConstraints constraints = new GridBagConstraints();
        textAreaOfResult = new JTextArea();
        DefaultCaret caret = (DefaultCaret) textAreaOfResult.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textAreaOfResult.setFocusable(false);
        textAreaOfResult.setEditable(false);
        textAreaOfResult.setForeground(new Color(24, 177, 0));
        textAreaOfResult.setBackground(Color.BLACK);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        JScrollPane textAreaScrollPane = new JScrollPane(textAreaOfResult);
        reverseShellDialog.add(textAreaScrollPane, constraints);
        constraints.weighty = 0.0;


        fieldOfCommands = new JTextField();
        fieldOfCommands.setForeground(Color.GREEN);
        fieldOfCommands.setBackground(Color.BLACK);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        fieldOfCommands.addActionListener(new SendCommandAction(this));

        reverseShellDialog.add(fieldOfCommands, constraints);

    }

    public Streams getStream() {
        return stream;
    }

    public JTextField getFieldOfCommands() {
        return fieldOfCommands;
    }

    public JTextArea getTextAreaOfResult() {
        return textAreaOfResult;
    }
}
