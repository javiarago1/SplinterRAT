package GUI.TableUtils.KeyboardController.MoveEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Mover implements ActionListener {

    private final JList<String> listOfEvents;
    private final int operation;

    public Mover(JList<String> listOfEvents, Movement movement) {
        operation = movement == Movement.UP ? -1 : 1;
        this.listOfEvents = listOfEvents;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) listOfEvents.getModel();
        int selectedIndex = listOfEvents.getSelectedIndex();
        String selectedName = listOfEvents.getSelectedValue();
        listModel.remove(selectedIndex);
        int newSelectedIndex = selectedIndex + operation;
        listModel.add(newSelectedIndex, selectedName);
        listOfEvents.setSelectedIndex(newSelectedIndex);
    }
}
