package GUI.TableUtils.KeyboardController;

import Connections.ClientHandler;
import Connections.Streams;
import GUI.SplinterGUI;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.Cleaner;
import java.util.Objects;

public class KeyboardControllerMenuListener implements ActionListener {

    private final SplinterGUI mainGUI;

    public KeyboardControllerMenuListener(SplinterGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ClientHandler clientHandler = GetSYS.getClientHandler();
        new KeyboardControllerGUI(mainGUI.getMainGUI(), clientHandler);
    }
}
