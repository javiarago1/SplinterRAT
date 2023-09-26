package Utilities;

import Server.Client;

public abstract class AbstractGUIManager<T extends AbstractDialogCreator> {
    private final T guiManager;

    public AbstractGUIManager(T guiManager) {
        this.guiManager = guiManager;
    }

    public Client getClient() {
        return guiManager.getClient();
    }

    public T getGUIManager() {
        return guiManager;
    }

}
