package GUI.TableUtils.Permissions.CheckAdmin;


import Connections.Streams;

import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class AdminPermissionAction implements ActionListener {


    public AdminPermissionAction() {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = Objects.requireNonNull(GetSYS.getStream(SocketType.MAIN));
        stream.getExecutor().submit(new AdminChecker(stream));

    }

}
