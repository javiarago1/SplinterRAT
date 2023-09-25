package Utilities;

import Updater.UpdaterFactory;
import Updater.UpdaterInterface;

public class SwingUpdaterFactory implements UpdaterFactory {
    @Override
    public UpdaterInterface createInstance() {
        return new SwingUpdater();
    }
}
