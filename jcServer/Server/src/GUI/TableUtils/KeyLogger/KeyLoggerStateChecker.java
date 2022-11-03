package GUI.TableUtils.KeyLogger;

import Connections.ClientErrorHandler;
import Connections.Streams;

import javax.swing.*;
import java.io.IOException;

public class KeyLoggerStateChecker extends SwingWorker<Void, Void> {
    private final Streams stream;
    private final JMenuItem[] keyloggerOptions;

    public KeyLoggerStateChecker(Streams stream, JMenuItem[] keyloggerOptions) {
        this.stream = stream;
        this.keyloggerOptions = keyloggerOptions;
    }
    Boolean stateOfKeylogger = false;
    @Override
    protected Void doInBackground() {

        try {
            stateOfKeylogger= stream.sendAndReadJSONX(KeyloggerEvents.STATE);
        } catch (IOException ex) {
            new ClientErrorHandler("Unable to get keylogger options, connection lost with client"
                    ,stream.getClientSocket());
        }
        return null;
    }

    @Override
    protected void done() {
        if (stateOfKeylogger) {
            keyloggerOptions[0].setVisible(false);
            keyloggerOptions[1].setVisible(true);
        } else {
            keyloggerOptions[0].setVisible(true);
            keyloggerOptions[1].setVisible(false);
        }
    }
}
