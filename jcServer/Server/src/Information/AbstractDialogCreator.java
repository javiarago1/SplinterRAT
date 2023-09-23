package Information;

import Connections.Client;

import javax.swing.*;

public class AbstractDialogCreator extends JDialog implements GUIManagerInterface {

    private final Client client;

    public AbstractDialogCreator(JFrame mainDialog, String title, Client client) {
        super(mainDialog, title + client.getIdentifier());
        this.client = client;
    }

    @Override
    public Client getClient() {
        return client;
    }
}
