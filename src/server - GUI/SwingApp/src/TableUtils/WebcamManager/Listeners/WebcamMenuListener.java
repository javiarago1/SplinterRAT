package TableUtils.WebcamManager.Listeners;

import Server.Client;
import Main.SplinterGUI;
import Utilities.GetSYS;
import Utilities.SwingUpdater;
import TableUtils.WebcamManager.WebcamGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WebcamMenuListener implements ActionListener {
    private final SplinterGUI mainGUI;

    public WebcamMenuListener(SplinterGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        assert client != null;
        if (!client.isWebcamDialogOpen()) {
            WebcamGUI webcamGUI = new WebcamGUI(client, mainGUI.getMainGUI());
            SwingUpdater swingUpdater = (SwingUpdater) client.updater;
            swingUpdater.setWebcamGUI(webcamGUI);
            webcamGUI.getDevices();
        }
    }
}
