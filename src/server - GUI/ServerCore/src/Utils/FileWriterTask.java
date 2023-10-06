package Utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileWriterTask implements Runnable {
    private final byte[] data;
    private final String outputPath;

    private final boolean shouldExtract;

    public FileWriterTask(byte[] data, String outputPath, boolean shouldExtract) {
        this.data = data;
        this.outputPath = outputPath;
        this.shouldExtract = shouldExtract;
    }

    public void unzipFileInMemory() {
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

    public void saveZipFile() {
        File outputFile = new File(outputPath + ".zip");
        try {
            // Crear directorios padre si no existen
            File parentDir = outputFile.getParentFile();
            if (!parentDir.exists()) {
                boolean dirsCreated = parentDir.mkdirs();
                if (!dirsCreated) {
                    System.err.println("No se pudieron crear los directorios padre para " + outputPath);
                    return;
                }
            }

            // Escribir el archivo
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (shouldExtract) {
            unzipFileInMemory();
        } else {
            saveZipFile();
        }
    }

}

