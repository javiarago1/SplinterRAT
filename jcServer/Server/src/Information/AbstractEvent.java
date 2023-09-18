package Information;

import Connections.Client;

public abstract class AbstractEvent<T extends GUIManagerInterface> extends AbstractGUIManager<T> implements Runnable {

    public AbstractEvent(T guiManager) {
        super(guiManager);
    }

    @Override
    public abstract void run();
}
