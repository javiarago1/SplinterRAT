package GUI.TableUtils.ScreenStreaming;

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
        Streams stream = Objects.requireNonNull(GetSYS.getClientHandler()).getMainStream();
        new ScreenStreamingGUI(stream);
    }
}
