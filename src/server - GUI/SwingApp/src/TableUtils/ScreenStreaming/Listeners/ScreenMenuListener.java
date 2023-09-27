package TableUtils.ScreenStreaming.Listeners;

import Server.Client;
import TableUtils.Permissions.Events.PrivilegesElevatorEvent;
import Utilities.GUIFactory;
import Utilities.GetSYS;
import Utilities.MenuListenerClientAssigner;
import Utilities.SwingUpdater;
import TableUtils.ScreenStreaming.ScreenStreamerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScreenMenuListener extends MenuListenerClientAssigner<ScreenStreamerGUI> {
    public ScreenMenuListener(GUIFactory<ScreenStreamerGUI> guiFactory) {
        super(guiFactory);
    }

}
