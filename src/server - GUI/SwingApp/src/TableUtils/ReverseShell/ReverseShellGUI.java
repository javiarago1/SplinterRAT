package TableUtils.ReverseShell;

import Server.Client;
import Main.Main;
import TableUtils.ReverseShell.Actions.SendCommandAction;
import TableUtils.ReverseShell.Events.StartShellEvent;
import TableUtils.ReverseShell.Listeners.ScreenWindowAdapter;
import Utilities.AbstractDialogCreator;

import javax.swing.*;
import java.awt.*;

public class ReverseShellGUI extends AbstractDialogCreator {
    private JTextField fieldOfCommands;
    private JTextArea textAreaOfResult;


    public ReverseShellGUI(Client client) {
        super(Main.gui, client, "Reverse shell");
        this.setSize(new Dimension(600, 400));
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        this.addWindowListener(new ScreenWindowAdapter(this));
        setupComponents();

        this.setVisible(true);
    }

    public void initializeShell(){
        getClient().getExecutor().submit(new StartShellEvent(this));
    }

    private void setupComponents() {
        GridBagConstraints constraints = new GridBagConstraints();

        // TextArea Configuration
        textAreaOfResult = new JTextArea();
        textAreaOfResult.setFocusable(false);
        textAreaOfResult.getCaret().setVisible(false);
        textAreaOfResult.setEditable(false);
        textAreaOfResult.setForeground(new Color(24, 177, 0));
        textAreaOfResult.setBackground(Color.BLACK);

        Font currentFont = textAreaOfResult.getFont();
        Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), 14);
        textAreaOfResult.setFont(newFont);

        constraints.insets = new Insets(5, 5,0,5);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 0.95;
        JScrollPane textAreaScrollPane = new JScrollPane(textAreaOfResult);
        textAreaScrollPane.setBorder(null);
        this.add(textAreaScrollPane, constraints);

        // TextField Configuration
        fieldOfCommands = new JTextField();
        fieldOfCommands.setForeground(Color.WHITE);
        fieldOfCommands.setCaretColor(Color.WHITE);
        constraints.gridy = 1;
        constraints.weighty = 0.05;  // TextField occupies less

        constraints.insets = new Insets(5, 5,5,5);
        fieldOfCommands.addActionListener(new SendCommandAction(this));
        this.add(fieldOfCommands, constraints);
    }

    public JTextField getFieldOfCommands() {
        return fieldOfCommands;
    }

    public JTextArea getTextAreaOfResult() {
        return textAreaOfResult;
    }
}
