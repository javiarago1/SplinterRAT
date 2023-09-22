package GUI.TableUtils.KeyboardController.Listeners;

import Connections.Client;
import Connections.ClientHandler;
import GUI.SplinterGUI;
import Connections.GetSYS;
import GUI.TableUtils.KeyboardController.KeyboardControllerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KeyboardControllerMenuListener implements ActionListener {

    private final SplinterGUI mainGUI;

    public KeyboardControllerMenuListener(SplinterGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandlerV2();
        new KeyboardControllerGUI(client);
    }
}
