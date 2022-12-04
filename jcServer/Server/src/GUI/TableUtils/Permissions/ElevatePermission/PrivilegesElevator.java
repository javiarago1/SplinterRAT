package GUI.TableUtils.Permissions.ElevatePermission;

import Connections.ClientErrorHandler;
import Connections.Streams;
import GUI.Main;
import GUI.TableUtils.Permissions.Permissions;

import javax.swing.*;
import java.io.IOException;


public class PrivilegesElevator implements Runnable{

    private final Streams stream;

    public PrivilegesElevator(Streams stream) {
        this.stream = stream;
    }

    @Override
    public void run() {
        try {
            int wasElevated = stream.sendAndReadAction(Permissions.ELEVATE_PRIVILEGES);
            switch (wasElevated){
                case 1 -> new ClientErrorHandler(
                        "The client accepted admin privileges, restarting client with privileges.",
                        stream.getClientSocket(),
                        JOptionPane.INFORMATION_MESSAGE);
                case 0-> JOptionPane.showMessageDialog(Main.gui.getMainGUI(),
                        "The client rejected the UAC prompt, no admin privileges where achieved.",
                        "Exception with client", JOptionPane.ERROR_MESSAGE);
                default -> JOptionPane.showMessageDialog(Main.gui.getMainGUI(),
                        "The client has admin privileges, no need to elevate.",
                        "Exception with client", JOptionPane.WARNING_MESSAGE);
            }

        } catch (IOException e) {
            new ClientErrorHandler("Unable to get privileges, connection lost with client.",
                    stream.getClientSocket());
        }
    }
}
