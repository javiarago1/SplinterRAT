package TableUtils.KeyboardController.Listeners;

import Server.Client;
import Main.SplinterGUI;
import Utilities.GUIFactory;
import Utilities.GetSYS;
import TableUtils.KeyboardController.KeyboardControllerGUI;
import Utilities.MenuListenerClientAssigner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KeyboardControllerMenuListener extends MenuListenerClientAssigner<KeyboardControllerGUI> {

    public KeyboardControllerMenuListener(GUIFactory<KeyboardControllerGUI> guiFactory) {
        super(guiFactory);
    }
}
