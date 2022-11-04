package GUI.TableUtils.KeyboardController;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListListener implements ListSelectionListener {
    private final JButton moveUpButton;
    private final JButton moveDownButton;
    private final JButton removeEventButton;
    private final JList<String> listOfEvents;

    ListListener(JList<String> listOfEvents, JButton[] listButtons) {
        this.listOfEvents = listOfEvents;
        moveUpButton = listButtons[0];
        moveDownButton = listButtons[1];
        removeEventButton = listButtons[2];
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {
        DefaultListModel<String> listModel = (DefaultListModel<String>) listOfEvents.getModel();
        boolean hasSelections = listOfEvents.getSelectedIndices().length > 0;
        if (!hasSelections) {
            moveUpButton.setEnabled(false);
            moveDownButton.setEnabled(false);
            removeEventButton.setEnabled(false);
        } else if (listModel.getSize() < 2) {
            moveUpButton.setEnabled(false);
            moveDownButton.setEnabled(false);
            removeEventButton.setEnabled(true);
        } else if (listOfEvents.getSelectedIndex() == 0) {
            moveUpButton.setEnabled(false);
            moveDownButton.setEnabled(true);
            removeEventButton.setEnabled(true);
        } else if (listOfEvents.getSelectedIndex() >= listModel.getSize() - 1) {
            moveDownButton.setEnabled(false);
            moveUpButton.setEnabled(true);
            removeEventButton.setEnabled(true);
        } else {
            moveUpButton.setEnabled(true);
            moveDownButton.setEnabled(true);
            removeEventButton.setEnabled(true);
        }
    }
}
