package Utilities.Event;

import Server.ConnectionStore;

import javax.swing.*;

import Main.Main;
import Utilities.AbstractDialogCreator;
import Utilities.AbstractGUIManager;

public abstract class AbstractEventGUI<T extends AbstractDialogCreator> extends AbstractGUIManager<T> implements AbstractEvent {

    public AbstractEventGUI(T guiManager) {
        super(guiManager);
    }

    @Override
    public abstract void run();

    @Override
    public void handleGuiError() {
        SwingUtilities.invokeLater(() -> {
            Main.gui.updateUserStateToDisconnected();
            JOptionPane.showMessageDialog(getGUIManager(), "Connection with the client has been lost.", "Error", JOptionPane.ERROR_MESSAGE);
            getGUIManager().closeDialog();
        });
        ConnectionStore.removeConnection(getClient().getUUID());
    }


}
