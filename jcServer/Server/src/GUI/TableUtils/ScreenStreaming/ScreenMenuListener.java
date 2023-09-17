package GUI.TableUtils.ScreenStreaming;

import Connections.Client;
import Connections.ClientHandler;
import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ScreenMenuListener implements ActionListener {


    public ScreenMenuListener() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandlerV2();
        assert client != null;
        ScreenStreamingGUI screenStreamingGUI = new ScreenStreamingGUI(client);
        client.updater.setScreenStreamerGUI(screenStreamingGUI);
    }
}
