package Utilities;

import Server.Client;

import java.awt.*;

public abstract class AbstractDialogCreatorWUpdater extends AbstractDialogCreator implements UpdateNeed {

    private final SwingUpdater swingUpdater;
    public AbstractDialogCreatorWUpdater(Window window, Client client, String title) {
        super(window, client, title);
        swingUpdater = (SwingUpdater) client.updater;
        addToSwingUpdater();
    }

    @Override
    public SwingUpdater getSwingUpdater() {
        return swingUpdater;
    }

}
