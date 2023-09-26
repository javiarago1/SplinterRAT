package TableUtils.MessageBox;

import Server.Client;
import Main.Main;
import TableUtils.MessageBox.Actions.MessageBoxAction;
import Utilities.AbstractDialogCreator;

import javax.swing.*;
import java.awt.*;

public class MessageBoxGUI extends AbstractDialogCreator {

    public MessageBoxGUI(Client client) {
        super(Main.gui, client ,"Message box");
        this.setLayout(new GridBagLayout());
        this.setSize(250, 350);
        this.setLocationRelativeTo(null);
        addComponents();
        this.setVisible(true);
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
        this.add(labelTitle, constraints);


        constraints.gridy = 1;
        titleTextField = new JTextField();
        this.add(titleTextField, constraints);

        constraints.gridy = 2;
        JLabel labelContent = new JLabel("Content for message box:");
        this.add(labelContent, constraints);

        constraints.weighty = 1;
        constraints.gridy = 3;
        contentTextArea = new JTextArea();
        contentTextArea.setLineWrap(true);
        this.add(new JScrollPane(contentTextArea), constraints);

        constraints.weighty = 0;
        constraints.gridy = 4;
        JLabel labelOfType = new JLabel("Select button of box:");
        this.add(labelOfType, constraints);

        constraints.gridy = 5;
        typeOfBox = new JComboBox<>();
        typeOfBox.addItem("Ok");
        typeOfBox.addItem("Ok, cancel");
        typeOfBox.addItem("Yes, no");
        typeOfBox.addItem("Yes, no, cancel");

        this.add(typeOfBox, constraints);


        constraints.gridy = 6;
        JLabel labelOfIcon = new JLabel("Select icon of box:");
        this.add(labelOfIcon, constraints);

        constraints.gridy = 7;
        iconOfBox = new JComboBox<>();
        iconOfBox.addItem("Error");
        iconOfBox.addItem("Question");
        iconOfBox.addItem("Warning");
        iconOfBox.addItem("Packets");

        this.add(iconOfBox, constraints);
        JButton sendBoxButton = new JButton("Send box");
        sendBoxButton.addActionListener(new MessageBoxAction(this));


        constraints.gridy = 8;
        this.add(sendBoxButton, constraints);

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


}
