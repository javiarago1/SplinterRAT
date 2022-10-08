package GUI.ProgressBar;

import Connections.Streams;
import Information.Action;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class UploadProgressBar extends Bar {
    private final Streams stream;
    private final File[] localFiles;
    private final List<String> destinationPaths;

    public UploadProgressBar(JDialog dialog, Streams stream, File[] localFiles, List<String> destinationPaths) {
        super(dialog, "Uploading");
        this.stream = stream;
        this.localFiles = localFiles;
        this.destinationPaths = destinationPaths;
    }

    @Override
    protected Void doInBackground() {
        int countOfFiles = 0; // counter for GUI
        System.out.println("am i at edt " + SwingUtilities.isEventDispatchThread());
        stream.sendJSON(Action.UPLOAD, destinationPaths, localFiles.length);
        for (File file : localFiles) {
            stream.readSize(); // Start sending information
            Path path = Path.of(String.valueOf(file));  // Converting to path for getting all bytes of file
            stream.sendString(file.getName()); // Sending file name
            // Adding new name file to GUI

            // Wait for response to start sending bytes
            //stream.readSize();

            try {
                stream.sendSize((int) Files.size(path)); // Sending size of bytes
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] fileBytes;
            try {
                fileBytes = Files.readAllBytes(path); // getting bytes of file
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            DataOutputStream dos = stream.getDos();
            int total = fileBytes.length; // total amount to send

            int read = 0;
            while (read < fileBytes.length) {
                int toRead = Math.min(fileBytes.length - read, 8192); // Read size by size with buffer -> 4096
                try {
                    dos.write(fileBytes, read, toRead);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                read += toRead;
                publish((int) Math.floor((float) read * 100 / total), (countOfFiles + 1) + "/" + localFiles.length + " " + file.getName()); // Publish to GUI % of upload
                System.out.println(read + "/" + total);
            }
            countOfFiles++;
            System.out.println("finished with first file ? ");
            //stream.readSize(); // Wait for response of process done

        }
        return null;
    }

}
