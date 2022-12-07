package GUI.TableUtils.ReverseShell;

import Connections.Streams;

import javax.swing.*;

import java.awt.*;


/*
   In this GUI I make the user believe that he is writing directly to the console,
   it really consists of a textarea and a text field that are responsible for updating the state
 */


public class ReverseShellGUI {
    private final Streams stream;
    private final JDialog reverseShellDialog;
    private JTextField fieldOfCommands;
    private JTextArea textAreaOfResult;
    private boolean pressedEnter = false;
    private String lastPath;

    public ReverseShellGUI(Streams stream, JFrame mainGUI) {
        this.stream = stream;
        reverseShellDialog = new JDialog(mainGUI, "Reverse shell -" + stream.getIdentifier());
        reverseShellDialog.setSize(new Dimension(500, 300));
        reverseShellDialog.setLocationRelativeTo(null);
        reverseShellDialog.setLayout(new GridBagLayout());
        addStyle();
        getCurrentPathAtStart();
        reverseShellDialog.setVisible(true);
    }


    // style of text area and field text

    private void addStyle() {
        GridBagConstraints constraints = new GridBagConstraints();
        textAreaOfResult = new JTextArea();
        textAreaOfResult.setFocusable(false);
        textAreaOfResult.getCaret().setVisible(true);
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
        textAreaScrollPane.setBorder(null);
        reverseShellDialog.add(textAreaScrollPane, constraints);
        constraints.weighty = 0.0;


        fieldOfCommands = new JTextField();
        fieldOfCommands.setForeground(Color.BLACK);
        fieldOfCommands.setBackground(Color.BLACK);
        fieldOfCommands.setBorder(null);
        fieldOfCommands.setCaretColor(Color.BLACK);
        fieldOfCommands.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        fieldOfCommands.addCaretListener(e -> textAreaOfResult.setCaretPosition(
                textAreaOfResult.getText().length()
                        - fieldOfCommands.getText().length()
                        + fieldOfCommands.getCaretPosition()));
        fieldOfCommands.addActionListener(new SendCommandAction(this));
        fieldOfCommands.getDocument().addDocumentListener(new FieldDocumentListener(this));
        reverseShellDialog.add(fieldOfCommands, constraints);

    }

    public String getLastPath() {
        return lastPath;
    }

    public void setLastPath(String lastPath) {
        this.lastPath = lastPath;
    }

    public boolean isPressedEnter() {
        return pressedEnter;
    }

    public void setPressedEnter(boolean pressedEnter) {
        this.pressedEnter = pressedEnter;
    }

    private void getCurrentPathAtStart() {
        stream.getExecutor().submit(new CommandSender(this, "ver"));
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
