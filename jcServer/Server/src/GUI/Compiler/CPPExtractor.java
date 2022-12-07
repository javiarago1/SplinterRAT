package GUI.Compiler;

import GUI.Main;

import net.lingala.zip4j.ZipFile;
import org.apache.commons.lang3.SystemUtils;


import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class CPPExtractor implements Runnable {


    @Override
    public void run() {
        try {
            String folderDestination = "";
            if (SystemUtils.IS_OS_WINDOWS) {
                folderDestination = System.getenv("APPDATA");

            } else if (SystemUtils.IS_OS_LINUX) {
                folderDestination = System.getProperty("user.home");
            }
            extractClientFilesFromJAR(folderDestination);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void extractClientFilesFromJAR(String folderDestination) throws IOException {
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
            stream = Main.class.getResourceAsStream(fileName);
            int readBytes;
            byte[] buffer = new byte[4096];
            if (stream != null) {
                while ((readBytes = stream.read(buffer)) > 0) {
                    resStreamOut.write(buffer, 0, readBytes);
                }
            } else {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "JAR files might be corrupted or JAR " +
                                "doesn't have enough permissions to extract files!",
                        "Error extracting files", JOptionPane.ERROR_MESSAGE));
            }
            zipFile.extractAll(folderOfTempFiles.toString());
            Compiler.localClientFiles = Path.of(folderOfTempFiles.toString(), finalFolderName);
            System.out.println(Compiler.localClientFiles);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}