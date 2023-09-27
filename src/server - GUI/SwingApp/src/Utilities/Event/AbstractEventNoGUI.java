package Utilities.Event;

import Main.Main;
import Server.Client;
import Server.ConnectionStore;

import javax.swing.*;

public abstract class AbstractEventNoGUI implements AbstractEvent{

    private final Client client;

    public AbstractEventNoGUI(Client client){
        this.client = client;
    }

    @Override
    public void handleGuiError(){
        SwingUtilities.invokeLater(() -> {
            Main.gui.updateUserStateToDisconnected();
            JOptionPane.showMessageDialog(null, "Connection with the client has been lost.", "Error", JOptionPane.ERROR_MESSAGE);
        });
        ConnectionStore.removeConnection(getClient().getSession());
    }
    @Override
    public Client getClient() {
        return client;
    }
    @Override
    public abstract void run();
}
