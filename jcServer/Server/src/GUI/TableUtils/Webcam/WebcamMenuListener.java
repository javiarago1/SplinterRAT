package GUI.TableUtils.Webcam;

import Connections.Streams;
import GUI.JsGUI;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Webcam.WebcamManager.WebcamGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class WebcamMenuListener implements ActionListener {

    private final ConcurrentHashMap<Socket, Streams> map;
    private final JTable table;

    private final JsGUI mainGUI;

    public WebcamMenuListener(JTable table, ConcurrentHashMap<Socket, Streams> map, JsGUI mainGUI) {
        this.mainGUI = mainGUI;
        this.map = map;
        this.table = table;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = GetSYS.getStream(map, table);
        assert stream != null;
        if (!stream.isWebcamDialogOpen()) new WebcamGUI(stream, mainGUI.getMainGUI());

    }
}
