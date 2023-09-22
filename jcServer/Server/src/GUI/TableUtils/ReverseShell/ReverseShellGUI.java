package GUI.TableUtils.ReverseShell;

import Connections.Client;
import GUI.Main;
import GUI.TableUtils.ReverseShell.Actions.SendCommandAction;
import GUI.TableUtils.ReverseShell.Events.StartShellEvent;
import GUI.TableUtils.ReverseShell.Listeners.ScreenWindowAdapter;
import Information.GUIManagerInterface;

import javax.swing.*;
import java.awt.*;

public class ReverseShellGUI implements GUIManagerInterface {
    private final JDialog reverseShellDialog;
    private JTextField fieldOfCommands;
    private JTextArea textAreaOfResult;
    private final Client client;

    public ReverseShellGUI(Client client) {
        this.client = client;
        reverseShellDialog = new JDialog(Main.gui.getMainGUI(), "Reverse shell -" + client.getIdentifier());
        reverseShellDialog.setSize(new Dimension(600, 400));
        reverseShellDialog.setLocationRelativeTo(null);
        reverseShellDialog.setLayout(new GridBagLayout());
        reverseShellDialog.addWindowListener(new ScreenWindowAdapter(this));
        setupComponents();

        reverseShellDialog.setVisible(true);
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
        reverseShellDialog.add(textAreaScrollPane, constraints);

        // TextField Configuration
        fieldOfCommands = new JTextField();
        fieldOfCommands.setForeground(Color.WHITE);
        fieldOfCommands.setCaretColor(Color.WHITE);
        constraints.gridy = 1;
        constraints.weighty = 0.05;  // TextField occupies less

        constraints.insets = new Insets(5, 5,5,5);
        fieldOfCommands.addActionListener(new SendCommandAction(this));
        reverseShellDialog.add(fieldOfCommands, constraints);
    }

    public JTextField getFieldOfCommands() {
        return fieldOfCommands;
    }

    public JTextArea getTextAreaOfResult() {
        return textAreaOfResult;
    }

    public Client getClient() {
        return client;
    }
}
