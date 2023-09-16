package GUI.TableUtils.FileManager.Actions;

import Connections.Client;
import Connections.ClientHandler;
import Connections.Streams;
import GUI.ProgressBar.DownloadProgressBar;
import GUI.TableUtils.Configuration.SocketType;
import GUI.TableUtils.FileManager.FileManagerGUI;

import java.awt.event.ActionEvent;

public class DownloadAction extends Manager {

    public DownloadAction(FileManagerGUI fileManagerGUI) {
        super(fileManagerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Client client = getFileManagerGUI().getClient();
        client.getExecutor().submit(new DownloadProgressBar(getFileManagerGUI().getFileManagerDialog(), client, getSelectedPaths()));
    }


}
