package GUI.TableUtils.KeyboardController.Listeners;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListListener implements ListSelectionListener {
    private final JButton moveUpButton;
    private final JButton moveDownButton;
    private final JButton removeEventButton;
    private final JList<String> listOfEvents;

    public ListListener(JList<String> listOfEvents, JButton[] listButtons) {
        this.listOfEvents = listOfEvents;
        moveUpButton = listButtons[0];
        moveDownButton = listButtons[1];
        removeEventButton = listButtons[2];
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) listOfEvents.getModel();
        int selectedIndex = listOfEvents.getSelectedIndex();
        int listSize = listModel.getSize();
        boolean hasSelections = selectedIndex != -1;

        if (!hasSelections) {
            moveUpButton.setEnabled(false);
            moveDownButton.setEnabled(false);
            removeEventButton.setEnabled(false);
            return;
        }

        removeEventButton.setEnabled(true);

        if (listSize < 2) {
            moveUpButton.setEnabled(false);
            moveDownButton.setEnabled(false);
            return;
        }

        moveUpButton.setEnabled(selectedIndex != 0);
        moveDownButton.setEnabled(selectedIndex != listSize - 1);
    }
}
