package GUI.TableUtils.MessageBox;

import Connections.ClientHandler;
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
        ClientHandler clientHandler = GetSYS.getClientHandler();
        new MessageBoxGUI(clientHandler);
    }
}