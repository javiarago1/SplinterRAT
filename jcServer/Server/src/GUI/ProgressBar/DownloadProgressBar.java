package GUI.ProgressBar;

import Connections.ClientErrorHandler;
import Connections.Streams;
import Information.Action;
import Information.FolderOpener;
import Information.Time;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.*;
import java.util.List;


public class DownloadProgressBar extends Bar {

    private final Streams stream;
    private final List<String> downloadList;
    private final String time;


    public DownloadProgressBar(JDialog parentDialog, Streams stream, List<String> downloadList) {
        super(parentDialog, "Downloading");
        this.stream = stream;
        this.downloadList = downloadList;
        time = new Time().getTime();
    }


    @Override
    protected Void doInBackground() {
        try {
            startDownload();
        } catch (IOException e) {
            new ClientErrorHandler("Unable to download, connection lost with client", getDialog(), stream.getClientSocket());
        }
        return null;
    }

    private void startDownload() throws IOException {
        stream.sendAction(Action.DOWNLOAD, downloadList);
        String tempPath;
        // receives files till string equals to "/". "/" = no more files to send
        String whereToDownload = stream.getSessionFolder() + "\\Downloaded Files\\" + time;
        while (!(tempPath = stream.readString()).equals("/")) {
            System.out.println("Route -> " + tempPath);
            // where to save the file
            String formedPath = whereToDownload + "\\" + tempPath;
            if (isOperating()) {
                stream.sendSize(0); // start sending bytes
                // Receive and create file if it doesn't exist (Relative folders created too)
                FileUtils.writeByteArrayToFile(new File(formedPath), receiveBytes(tempPath));
            } else {
                stream.sendSize(-1); // start sending bytes
            }

        }
        FolderOpener.open(whereToDownload);

    }

    public byte[] receiveBytes(String fileName) throws IOException {
        int fileSize = stream.readSize(); // get file size
        byte[] buffer = new byte[fileSize];
        DataInputStream dis = stream.getDis();
        int total = 0;

        while (total < fileSize) {
            int read = dis.read(buffer, total, fileSize - total); // read to buffer
            total += read; // total readed
            publish((int) Math.floor((float) total * 100 / fileSize), fileName);     // publish % and file name
            System.out.println(total + " /" + fileSize);
        }

        return buffer;
    }

}
