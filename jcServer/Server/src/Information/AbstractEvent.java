package Information;

import Connections.Client;

public abstract class AbstractEvent<T extends GUIManagerInterface> implements Runnable {
    private final T guiManager;

    public AbstractEvent(T guiManager) {
        this.guiManager = guiManager;
    }

    public Client getClient() {
        return guiManager.getClient();
    }

    public T getGUIManager() {
        return guiManager;
    }

    @Override
    public abstract void run();
}
