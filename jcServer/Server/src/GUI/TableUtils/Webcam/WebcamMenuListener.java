package GUI.TableUtils.Webcam;

import Connections.ClientHandler;
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

    public WebcamMenuListener(SplinterGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ClientHandler clientHandler = GetSYS.getClientHandler();
        Streams stream = GetSYS.getStream(SocketType.WEBCAM);
        if (stream.isWebcamDialogOpen()) new WebcamGUI(clientHandler, mainGUI.getMainGUI());

    }
}
