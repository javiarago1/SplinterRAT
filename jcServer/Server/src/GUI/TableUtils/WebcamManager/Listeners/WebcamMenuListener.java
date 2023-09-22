package GUI.TableUtils.WebcamManager.Listeners;

import Connections.Client;
import GUI.SplinterGUI;
import Connections.GetSYS;
import GUI.TableUtils.WebcamManager.WebcamGUI;

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
        assert client != null;
        if (!client.isWebcamDialogOpen()) {
            WebcamGUI webcamGUI = new WebcamGUI(client, mainGUI.getMainGUI());
            client.updater.setWebcamGUI(webcamGUI);
            webcamGUI.getDevices();
        }
    }
}
