package GUI.TableUtils.ReverseShell;

import Connections.Streams;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReverseShellGUI {
    private final Streams stream;
    JDialog reverseShellDialog;

    public ReverseShellGUI(Streams stream, JFrame mainGUI) {
        this.stream = stream;
        reverseShellDialog = new JDialog(mainGUI, "Reverse shell -" + stream.getIdentifier());
        reverseShellDialog.setSize(new Dimension(500, 300));
        reverseShellDialog.setLocationRelativeTo(null);
        reverseShellDialog.setLayout(new GridBagLayout());
        addStyle();
        reverseShellDialog.setVisible(true);
    }


    JTextField fieldOfCommands;
    JTextArea textAreaOfResult;
    int characterCounter = 0;

    public int getCharacterCounter() {
        return characterCounter;
    }

    private void addStyle() {
        GridBagConstraints constraints = new GridBagConstraints();
        textAreaOfResult = new JTextArea();
        DefaultCaret caret = (DefaultCaret) textAreaOfResult.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        ReverseShellGUI a = this;
        textAreaOfResult.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                ((AbstractDocument) textAreaOfResult.getDocument()).setDocumentFilter(new DocumentFilter() {
                    @Override
                    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                        //Won't remove
                        //new Exception("remove " + offset + "," + length).printStackTrace();
                    }

                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                            throws BadLocationException {
                        if (characterCounter > 0) {
                            super.replace(fb, offset, length, text, attrs);
                        }
                    }
                });
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("enter");
                    stream.getExecutor().submit(new CommandSender(a));
                } else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE || e.getKeyChar() == KeyEvent.VK_DELETE) {
                    characterCounter--;

                } else {
                    characterCounter++;
                }
            }
        });
        textAreaOfResult.getCaret().setVisible(true);
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
        fieldOfCommands.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Pattern matcher = Pattern.compile("[a-zA-Z]");
                Matcher mat = matcher.matcher(String.valueOf(e.getKeyChar()));
                int counter = 0;
                while (mat.find() && counter == 0) {
                    counter++;
                    System.out.println("xd");
                }
                if (counter > 0) System.out.println("ENCOUNTRED XD");
                if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE || e.getKeyChar() == KeyEvent.VK_DELETE) {
                    if (fieldOfCommands.getText().length() > 0)
                        textAreaOfResult.setText(textAreaOfResult.getText().substring(0, textAreaOfResult.getText().length() - 1));
                } else if (true) {
                    textAreaOfResult.append(String.valueOf(e.getKeyChar()));
                }
            }
        });
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
