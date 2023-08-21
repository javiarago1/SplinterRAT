package GUI.TableUtils.Permissions.ElevatePermission;

import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class ElevatePermissionAction implements ActionListener {

    public ElevatePermissionAction() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = Objects.requireNonNull(GetSYS.getStream(SocketType.PERMISSION));
        stream.getExecutor().submit(new PrivilegesElevator(stream));

    }
}
