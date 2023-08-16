package GUI.TableUtils.Webcam;

import Connections.Streams;
import GUI.Main;
import GUI.SplinterGUI;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;
import GUI.TableUtils.Webcam.WebcamManager.WebcamGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WebcamMenuListener implements ActionListener {
    private final SplinterGUI mainGUI;

    public WebcamMenuListener() {
        this.mainGUI = Main.gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = GetSYS.getClientHandler().getMainStream();
        if (stream.isWebcamDialogOpen()) new WebcamGUI(stream, mainGUI.getMainGUI());

    }
}
