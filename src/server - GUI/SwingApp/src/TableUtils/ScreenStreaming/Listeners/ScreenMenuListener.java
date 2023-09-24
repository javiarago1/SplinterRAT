package TableUtils.ScreenStreaming.Listeners;

import Server.Client;
import Utilities.GetSYS;
import Utilities.SwingUpdater;
import TableUtils.ScreenStreaming.ScreenStreamerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScreenMenuListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        ScreenStreamerGUI screenStreamerGUI = new ScreenStreamerGUI(client);
        SwingUpdater swingUpdater = (SwingUpdater) client.updater;
        swingUpdater.setScreenStreamerGUI(screenStreamerGUI);
        screenStreamerGUI.requestMonitors();
    }
}
