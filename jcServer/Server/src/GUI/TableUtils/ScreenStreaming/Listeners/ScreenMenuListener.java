package GUI.TableUtils.ScreenStreaming.Listeners;

import Connections.Client;
import Connections.GetSYS;
import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScreenMenuListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        ScreenStreamerGUI screenStreamerGUI = new ScreenStreamerGUI(client);
        client.updater.setScreenStreamerGUI(screenStreamerGUI);
        screenStreamerGUI.requestMonitors();
    }
}
