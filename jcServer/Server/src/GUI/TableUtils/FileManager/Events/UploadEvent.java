package GUI.TableUtils.FileManager.Events;

import Connections.BytesChannel;
import Connections.UniqueByteIDGenerator;
import GUI.ProgressBar.Bar;
import GUI.TableUtils.FileManager.FileManagerGUI;
import Information.AbstractEvent;
import net.lingala.zip4j.ZipFile;
import org.json.JSONObject;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UploadEvent extends AbstractEvent<FileManagerGUI> {
    private final File[] selectedFiles;
    private final Bar<?> progressBar;
    private final String selectedPath;
    private final AtomicBoolean cancellationAtomic;

    public UploadEvent(FileManagerGUI guiManager, Bar<?> progressBar, File[] selectedFiles, String selectedPath, AtomicBoolean cancellationAtomic) {
        super(guiManager);
        this.selectedFiles = selectedFiles;
        this.progressBar = progressBar;
        this.selectedPath = selectedPath;
        this.cancellationAtomic = cancellationAtomic;
    }

    @Override
    public void run() {
        UniqueByteIDGenerator uniqueByteIDGeneratorOut = getClient().getUniqueByteIDGeneratorOut();
        byte id = uniqueByteIDGeneratorOut.getID();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "PREPARE_UPLOAD");
        jsonObject.put("channel_id", id);
        jsonObject.put("to_path", selectedPath);
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            // 1. Comprimir usando zip4j
            String tempZipPath = "temp.zip";
            ZipFile zipFile = new ZipFile(tempZipPath);
            zipFile.addFiles(Arrays.asList(selectedFiles));

            // 2. Divide el archivo comprimido y envía en trozos
            byte[] buffer = new byte[204800 - 2];
            int read;

            FileInputStream fis = new FileInputStream(tempZipPath);

            boolean isLastPacket;
            while ((read = fis.read(buffer)) != -1 && cancellationAtomic.get()) {
                isLastPacket = (read < buffer.length);
                ByteBuffer byteBuffer = ByteBuffer.allocate(read + 2);
                byteBuffer.put(id);
                byteBuffer.put((byte) (isLastPacket ? 1 : 0));
                byteBuffer.put(buffer, 0, read);
                byteBuffer.flip();
                int finalRead = read;
                boolean finalIsLastPacket = isLastPacket;
                SwingUtilities.invokeLater(() -> progressBar.updateProgress(finalRead, finalIsLastPacket));
                getClient().sendBytes(byteBuffer);

            }

            fis.close();

            // Eliminar el archivo temporal
            new File(tempZipPath).delete();

            // 3. Libera el ID único
            uniqueByteIDGeneratorOut.finishTask(id);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
