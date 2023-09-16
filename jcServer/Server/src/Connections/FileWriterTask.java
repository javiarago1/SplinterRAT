package Connections;

import java.io.FileOutputStream;
import java.io.IOException;

public class FileWriterTask implements Runnable {
    private final byte[] data;
    private final String outputPath;

    public FileWriterTask(byte[] data, String outputPath) {
        this.data = data;
        this.outputPath = outputPath;
    }

    @Override
    public void run() {
        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            fos.write(data);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

