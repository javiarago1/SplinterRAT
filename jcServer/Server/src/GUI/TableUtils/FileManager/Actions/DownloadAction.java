package GUI.TableUtils.FileManager.Actions;

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
        // ClientHandler clientHandler = getFileManagerGUI().getClientHandler();
        // Streams stream = clientHandler.getStreamByName(SocketType.DOWNLOAD_UPLOAD);
        // stream.getExecutor().submit(new DownloadProgressBar(getFileManagerGUI().getFileManagerDialog(), clientHandler, stream,getSelectedPaths()));
    }


}
