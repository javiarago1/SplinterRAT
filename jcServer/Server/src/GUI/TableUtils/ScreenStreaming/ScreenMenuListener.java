package GUI.TableUtils.ScreenStreaming;

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
        ClientHandler clientHandler = GetSYS.getClientHandler();
        assert clientHandler != null;
        new ScreenStreamingGUI(clientHandler);
    }
}
