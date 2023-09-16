package Connections;

import Information.Time;
import net.lingala.zip4j.ZipFile;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileWriterTask implements Runnable {
    private final byte[] data;
    private final String outputPath;


    public FileWriterTask(byte[] data, String outputPath) {
        this.data = data;
        this.outputPath = outputPath;
    }


    @Override
    public void run() {
        File folder = new File(outputPath);
        boolean result = folder.mkdirs();
        if (result) System.out.println("Folder created!");
        try (InputStream bais = new ByteArrayInputStream(data);
             ZipInputStream zis = new ZipInputStream(bais)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File outputFile = new File(folder, entry.getName());
                if (entry.isDirectory()) {
                    outputFile.mkdirs();
                } else {
                    outputFile.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

