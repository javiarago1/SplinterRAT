package GUI.ProgressBar;

import Connections.ClientErrorHandler;
import Connections.ClientHandler;
import Connections.Streams;
import Information.Action;

import javax.swing.*;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class UploadProgressBar extends Bar {
    private final Streams stream;
    private final File[] localFiles;
    private final String destinationPath;

    public UploadProgressBar(JDialog dialog, Streams stream, File[] localFiles, String destinationPath) {
        super(dialog, "Uploading");
        this.stream = stream;
        this.localFiles = localFiles;
        this.destinationPath = destinationPath;
    }

    @Override
    protected Void doInBackground() {
        try {
            uploadFiles();
        } catch (IOException e) {
            new ClientErrorHandler("Unable to upload, connection lost with client", getDialog(), stream.getClientSocket());
        }
        return null;
    }

    private void uploadFiles() throws IOException {
        int countOfFiles = 0; // counter for GUI
        stream.sendAction(Action.UPLOAD, destinationPath, localFiles.length);
        boolean cancel = false;
        for (int i = 0; i < localFiles.length && !cancel; i++) {
            File file = localFiles[i];
            stream.readSize(); // Start sending information
            Path path = Path.of(String.valueOf(file));  // Converting to path for getting all bytes of file
            stream.sendString(file.getName()); // Sending file name
            // Adding new name file to GUI

            // Wait for response to start sending bytes
            //stream.readSize();


            stream.sendSize((int) Files.size(path)); // Sending size of bytes

            byte[] fileBytes;

            fileBytes = Files.readAllBytes(path); // getting bytes of file

            DataOutputStream dos = stream.getDos();
            int total = fileBytes.length; // total amount to send

            int read = 0;
            while (read < fileBytes.length) {
                stream.readSize();
                if (!isOperating()) {
                    stream.sendSize(-1);
                    cancel = true;
                    break;
                }
                stream.sendSize(0);


                int toRead = Math.min(fileBytes.length - read, 8192); // Read size by size with buffer -> 4096

                dos.write(fileBytes, read, toRead);

                read += toRead;
                publish((int) Math.floor((float) read * 100 / total), (countOfFiles + 1) + "/" + localFiles.length + " " + file.getName()); // Publish to GUI % of upload
                System.out.println(read + "/" + total);
            }
            countOfFiles++;
            System.out.println("finished with first file ? ");
            //stream.readSize(); // Wait for response of process done

        }
    }

}
