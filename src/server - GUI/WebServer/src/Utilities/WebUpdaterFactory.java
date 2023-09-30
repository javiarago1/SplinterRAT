package Utilities;

import Updater.UpdaterFactory;
import Updater.UpdaterInterface;

public class WebUpdaterFactory implements UpdaterFactory {
    @Override
    public UpdaterInterface createInstance() {
        return new WebUpdater();
    }
}
