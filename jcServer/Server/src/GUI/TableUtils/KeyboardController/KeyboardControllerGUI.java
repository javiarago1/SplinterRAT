package GUI.TableUtils.KeyboardController;

import Connections.Streams;
import GUI.TableUtils.KeyboardController.MoveEvent.Movement;
import GUI.TableUtils.KeyboardController.MoveEvent.Mover;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class KeyboardControllerGUI {
    JDialog dialog;
    DefaultListModel<String> listModel = new DefaultListModel<>();
    JList<String> listOfEvents;

    public KeyboardControllerGUI(JFrame mainGUI, Streams stream) {
        dialog = new JDialog();
        dialog.setSize(new Dimension(650, 400));
        dialog.setLayout(new GridLayout(1, 2));
        dialog.setLocationRelativeTo(null);
        leftPanel();
        rightPanel();
        dialog.setVisible(true);
    }


    private void leftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel eventsListLabel = new JLabel("List of labels");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.insets = new Insets(2, 3, 2, 3);
        leftPanel.add(eventsListLabel, constraints);

        JButton moveUpButton;

        listOfEvents = new JList<>(listModel);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 4;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        constraints.weightx = 0;
        JScrollPane listScrollPane = new JScrollPane(listOfEvents);
        leftPanel.add(listScrollPane, constraints);
        constraints.weighty = 0.0;

        moveUpButton = new JButton("Up");
        moveUpButton.setEnabled(false);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        moveUpButton.addActionListener(new Mover(listOfEvents, Movement.UP));
        leftPanel.add(moveUpButton, constraints);


        JButton moveDownButton = new JButton("Down");
        moveDownButton.setEnabled(false);
        constraints.gridx = 1;
        moveDownButton.addActionListener(new Mover(listOfEvents, Movement.DOWN));
        leftPanel.add(moveDownButton, constraints);


        JButton removeEventButton = new JButton("Remove");
        removeEventButton.setEnabled(false);
        constraints.gridx = 2;
        removeEventButton.addActionListener(e -> {
                    int selectedIndex = listOfEvents.getSelectedIndex();
                    listModel.remove(listOfEvents.getSelectedIndex());
                    if (selectedIndex == listModel.getSize())
                        listOfEvents.setSelectedIndex(selectedIndex - 1);
                    else listOfEvents.setSelectedIndex(selectedIndex);
                }
        );
        leftPanel.add(removeEventButton, constraints);


        JButton clearAllEventsButton = new JButton("Clear all");
        clearAllEventsButton.addActionListener(e -> listModel.removeAllElements());
        constraints.gridx = 3;
        leftPanel.add(clearAllEventsButton, constraints);

        listOfEvents.addListSelectionListener(new ListListener(listOfEvents, new JButton[]{
                moveUpButton,
                moveDownButton,
                removeEventButton
        }));

        dialog.add(leftPanel);

    }

    private void rightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.red);
        rightPanel.setLayout(new GridBagLayout());


        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weighty = 0;
        constraints.weightx = 1.0;
        constraints.insets = new Insets(2, 3, 2, 3);
        JLabel delayLabel = new JLabel("Delay (ms)");
        rightPanel.add(delayLabel, constraints);

        constraints.gridy = 1;
        JTextField delayTextField = new JTextField();

        rightPanel.add(delayTextField, constraints);

        constraints.gridy = 2;
        JButton delayAddButton = new JButton("Add delay");
        delayAddButton.setEnabled(false);

        delayTextField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent evt) {
                String delayText = delayTextField.getText();
                delayAddButton.setEnabled(delayText.length() > 0 && StringUtils.isNumeric(delayText));
            }
        });

        rightPanel.add(delayAddButton, constraints);
        delayAddButton.addActionListener(e -> {
            listModel.addElement("Delay: " + delayTextField.getText() + " ms");
            setSelectionToLastElement();
        });

        constraints.gridy = 3;
        rightPanel.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);

        constraints.gridy = 4;
        JLabel labelOfTextArea = new JLabel("Add text to be written");
        rightPanel.add(labelOfTextArea, constraints);

        constraints.gridy = 5;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        JTextArea textAreaToWrite = new JTextArea("Hello World!");
        textAreaToWrite.setLineWrap(true);
        JScrollPane textAreaScrollPane = new JScrollPane(textAreaToWrite);
        rightPanel.add(textAreaScrollPane, constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridy = 6;
        constraints.weighty = 0.0;
        JButton textAreaButton = new JButton("Add text");
        textAreaButton.setEnabled(false);
        textAreaToWrite.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                textAreaButton.setEnabled(textAreaToWrite.getText().length() > 0);
            }
        });
        textAreaButton.addActionListener(e -> {
            listModel.addElement("Text to write: " + textAreaToWrite.getText());
            textAreaToWrite.setText("");
            setSelectionToLastElement();
        });
        rightPanel.add(textAreaButton, constraints);

        constraints.gridy = 7;
        rightPanel.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);


        JLabel labelComboBox = new JLabel("Select especial key");
        constraints.gridy = 9;
        rightPanel.add(labelComboBox, constraints);

        constraints.gridy = 10;
        JComboBox<String> comboSpecialKeys = new JComboBox<>();
        comboSpecialKeys.addItem("Enter key");
        comboSpecialKeys.addItem("Left Windows key");
        comboSpecialKeys.addItem("Tab key");
        rightPanel.add(comboSpecialKeys, constraints);

        constraints.gridy = 11;
        JButton addSpecialEventButton = new JButton("Add to list");
        addSpecialEventButton.addActionListener(e -> {
            listModel.addElement("Special key: " + comboSpecialKeys.getSelectedItem());
            setSelectionToLastElement();
        });
        rightPanel.add(addSpecialEventButton, constraints);

        constraints.gridy = 12;
        rightPanel.add(new JSeparator(SwingConstants.HORIZONTAL), constraints);

        constraints.gridy = 13;
        JButton submitSequenceToClient = new JButton("Submit request to client");
        rightPanel.add(submitSequenceToClient, constraints);


        dialog.add(rightPanel);
    }

    private void setSelectionToLastElement() {
        listOfEvents.setSelectedIndex(listModel.getSize() - 1);
    }


    public static void main(String[] args) {
        new KeyboardControllerGUI(null, null);
    }
}
