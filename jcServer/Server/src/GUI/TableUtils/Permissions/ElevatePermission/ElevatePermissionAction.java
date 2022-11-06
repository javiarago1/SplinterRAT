package GUI.TableUtils.Permissions.ElevatePermission;

import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Permissions.CheckAdmin.AdminChecker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ElevatePermissionAction implements ActionListener {

    private final ConcurrentHashMap<Socket, Streams> map;
    private final JTable table;


    public ElevatePermissionAction(JTable table, ConcurrentHashMap<Socket, Streams> map) {
        this.map = map;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = GetSYS.getStream(map, table);
        assert stream != null;
        stream.getExecutor().submit(new PrivilegesElevator(stream));

    }
}
