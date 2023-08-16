package GUI.TableUtils.KeyLogger;

import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.util.Objects;

public class KeyLoggerMenuListener implements MenuListener{

    private final JMenuItem[] keyloggerOptions;

    public KeyLoggerMenuListener(JMenuItem[] keyloggerOptions) {
        this.keyloggerOptions = keyloggerOptions;
    }
    @Override
    public void menuSelected(MenuEvent e) {
        Streams stream = Objects.requireNonNull(GetSYS.getClientHandler()).getMainStream();
        stream.getExecutor().submit(new KeyLoggerStateChecker(stream,keyloggerOptions));

    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }

}