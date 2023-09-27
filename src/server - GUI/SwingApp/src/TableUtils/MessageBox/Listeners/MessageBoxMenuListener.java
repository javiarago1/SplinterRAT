package TableUtils.MessageBox.Listeners;

import Server.Client;
import Utilities.GUIFactory;
import Utilities.GetSYS;
import TableUtils.MessageBox.MessageBoxGUI;
import Utilities.MenuListenerClientAssigner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageBoxMenuListener extends MenuListenerClientAssigner<MessageBoxGUI> {
    public MessageBoxMenuListener(GUIFactory<MessageBoxGUI> guiFactory) {
        super(guiFactory);
    }
}