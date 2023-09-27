package Utilities;

import Server.Client;

@FunctionalInterface
public interface GUIFactory<T extends AbstractDialogCreator> {
    void create(Client client);
}