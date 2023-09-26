package Utilities;

import Server.ConnectionStore;

import javax.swing.*;

import Main.Main;

public abstract class AbstractEventGUI<T extends AbstractDialogCreator> extends AbstractGUIManager<T> implements AbstractEvent {

    public AbstractEventGUI(T guiManager) {
        super(guiManager);
    }

    @Override
    public abstract void run();

    @Override
    public void handleGuiError() {
        SwingUtilities.invokeLater(() -> {
            Main.gui.updateUserStateToDisconnected(getClient().getUUID());
            JOptionPane.showMessageDialog(getGUIManager(), "Connection with the client has been lost.", "Error", JOptionPane.ERROR_MESSAGE);
        });
        ConnectionStore.removeConnection(getClient().getSession());
    }


}
