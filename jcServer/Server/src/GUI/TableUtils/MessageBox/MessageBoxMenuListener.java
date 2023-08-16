package GUI.TableUtils.MessageBox;

import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageBoxMenuListener implements ActionListener {

    public MessageBoxMenuListener() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = GetSYS.getClientHandler().getMainStream();
        new MessageBoxGUI(stream);
    }
}