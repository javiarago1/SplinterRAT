package GUI.TableUtils.MessageBox;

import Connections.ClientHandler;
import Connections.Streams;
import GUI.Main;
import GUI.TableUtils.Configuration.SocketType;

import javax.swing.*;
import java.awt.*;

public class MessageBoxGUI {
    private final Streams stream;
    JDialog messageBoxDialog;

    public MessageBoxGUI(ClientHandler clientHandler) {
        this.stream = clientHandler.getStreamByName(SocketType.MAIN);
        messageBoxDialog = new JDialog(Main.gui.getMainGUI(), "Message box - " + clientHandler.getIdentifier());
        messageBoxDialog.setLayout(new GridBagLayout());
        messageBoxDialog.setSize(250, 350);
        messageBoxDialog.setLocationRelativeTo(null);
        addComponents();
        messageBoxDialog.setVisible(true);
    }

    private JTextArea contentTextArea;
    private JTextField titleTextField;
    private JComboBox<String> typeOfBox;
    private JComboBox<String> iconOfBox;

    private void addComponents() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weighty = 0.0;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.fill = GridBagConstraints.BOTH;

        JLabel labelTitle = new JLabel("Title for message box:");
        messageBoxDialog.add(labelTitle, constraints);


        constraints.gridy = 1;
        titleTextField = new JTextField();
        messageBoxDialog.add(titleTextField, constraints);

        constraints.gridy = 2;
        JLabel labelContent = new JLabel("Content for message box:");
        messageBoxDialog.add(labelContent, constraints);

        constraints.weighty = 1;
        constraints.gridy = 3;
        contentTextArea = new JTextArea();
        contentTextArea.setLineWrap(true);
        messageBoxDialog.add(new JScrollPane(contentTextArea), constraints);

        constraints.weighty = 0;
        constraints.gridy = 4;
        JLabel labelOfType = new JLabel("Select button of box:");
        messageBoxDialog.add(labelOfType, constraints);

        constraints.gridy = 5;
        typeOfBox = new JComboBox<>();
        typeOfBox.addItem("Ok");
        typeOfBox.addItem("Ok, cancel");
        typeOfBox.addItem("Yes, no");
        typeOfBox.addItem("Yes, no, cancel");

        messageBoxDialog.add(typeOfBox, constraints);


        constraints.gridy = 6;
        JLabel labelOfIcon = new JLabel("Select icon of box:");
        messageBoxDialog.add(labelOfIcon, constraints);

        constraints.gridy = 7;
        iconOfBox = new JComboBox<>();
        iconOfBox.addItem("Error");
        iconOfBox.addItem("Question");
        iconOfBox.addItem("Warning");
        iconOfBox.addItem("Information");

        messageBoxDialog.add(iconOfBox, constraints);
        JButton sendBoxButton = new JButton("Send box");
        sendBoxButton.addActionListener(new MessageBoxAction(this));


        constraints.gridy = 8;
        messageBoxDialog.add(sendBoxButton, constraints);

    }

    public JDialog getMessageBoxDialog() {
        return messageBoxDialog;
    }

    public JTextArea getContentTextArea() {
        return contentTextArea;
    }

    public JTextField getTitleTextField() {
        return titleTextField;
    }

    public JComboBox<String> getTypeOfBox() {
        return typeOfBox;
    }

    public JComboBox<String> getIconOfBox() {
        return iconOfBox;
    }

    public Streams getStream() {
        return stream;
    }

}
