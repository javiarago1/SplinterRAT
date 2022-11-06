package GUI.TableUtils.Permissions.CheckAdmin;


import Connections.Streams;

import GUI.TableUtils.Configuration.GetSYS;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class AdminPermissionAction implements ActionListener {

    private final ConcurrentHashMap<Socket, Streams> map;
    private final JTable table;


    public AdminPermissionAction(JTable table, ConcurrentHashMap<Socket, Streams> map) {
        this.map = map;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = GetSYS.getStream(map, table);
        assert stream != null;
        stream.getExecutor().submit(new AdminChecker(stream));

    }

}
