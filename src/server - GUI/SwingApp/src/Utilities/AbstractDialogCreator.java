package Utilities;

import Server.Client;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractDialogCreator extends JDialog implements GUIManagerInterface {
    private final Client client;
    public AbstractDialogCreator(Window window, Client client, String title) {
        super(window, title + client.getIdentifier());
        this.client = client;
    }

    @Override
    public Client getClient() {
        return client;
    }

    @Override
    public void closeDialog() {
        this.dispose();
    }
}
