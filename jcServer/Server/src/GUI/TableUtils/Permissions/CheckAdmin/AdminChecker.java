package GUI.TableUtils.Permissions.CheckAdmin;

import Connections.ClientErrorHandler;
import Connections.Streams;
import GUI.Main;
import GUI.TableUtils.Permissions.Permissions;

import javax.swing.*;
import java.io.IOException;

public class AdminChecker extends SwingWorker<Void,Void> {

    private final Streams stream;

    public AdminChecker(Streams stream) {
        this.stream = stream;
    }

    boolean isAdmin;
    @Override
    protected Void doInBackground() {

        try {

            isAdmin = stream.sendAndReadJSON(Permissions.IS_ADMIN);
        } catch (IOException e) {
            new ClientErrorHandler("Unable to get privileges, connection lost with client",
                    stream.getClientSocket());
        }
        return null;
    }

    @Override
    protected void done() {
        String message = isAdmin ? "Client has admin privileges" : "Client doesn't have admin privileges";
        int typeOfMessage = isAdmin ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE;
        JOptionPane.showMessageDialog(Main.gui.getMainGUI(),
                message,
                "Client privileges",
                typeOfMessage);
    }
}
