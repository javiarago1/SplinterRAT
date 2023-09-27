package TableUtils.ReverseShell.Listeners;



import Utilities.GUIFactory;
import Utilities.GetSYS;
import Main.SplinterGUI;
import TableUtils.ReverseShell.ReverseShellGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Server.Client;
import Utilities.MenuListenerClientAssigner;
import Utilities.SwingUpdater;

public class ReverseShellMenuListener extends MenuListenerClientAssigner<ReverseShellGUI> {
    public ReverseShellMenuListener(GUIFactory<ReverseShellGUI> guiFactory) {
        super(guiFactory);
    }

}
