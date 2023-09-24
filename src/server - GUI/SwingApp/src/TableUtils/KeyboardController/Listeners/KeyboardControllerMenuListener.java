package TableUtils.KeyboardController.Listeners;

import Server.Client;
import Main.SplinterGUI;
import Utilities.GetSYS;
import TableUtils.KeyboardController.KeyboardControllerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KeyboardControllerMenuListener implements ActionListener {

    private final SplinterGUI mainGUI;

    public KeyboardControllerMenuListener(SplinterGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        new KeyboardControllerGUI(client);
    }
}
