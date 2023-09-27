package TableUtils.WebcamManager.Listeners;

import Server.Client;
import Main.SplinterGUI;
import Utilities.GUIFactory;
import Utilities.GetSYS;
import Utilities.MenuListenerClientAssigner;
import Utilities.SwingUpdater;
import TableUtils.WebcamManager.WebcamGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WebcamMenuListener extends MenuListenerClientAssigner<WebcamGUI> {
    public WebcamMenuListener(GUIFactory<WebcamGUI> guiFactory) {
        super(guiFactory);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = GetSYS.getClientHandler();
        if (client != null && !client.isWebcamDialogOpen()) {
             getGuiFactory().create(client);
        }
    }
}
