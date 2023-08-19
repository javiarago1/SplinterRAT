package Connections;

import GUI.TableUtils.Configuration.SocketType;
import Information.NetworkInformation;
import Information.SystemInformation;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


public class ClientHandler {
    private final ConcurrentHashMap<String, Streams> map = new ConcurrentHashMap<>();
    private SystemInformation tempSystemInformation;
    private NetworkInformation tempNetworkInformation;

    public void addStream(Socket socket) {
        Streams stream;
        String nameOfOperation;
        try {
            stream = new Streams(socket);
            stream.sendString("WAU");
            nameOfOperation = stream.readString();
            map.put(nameOfOperation, stream);
            System.out.println("Establecido nuevo socket! "+nameOfOperation);
            if (nameOfOperation.equals("MAIN")){
                stream.setMainStream(stream);
            } else {
                stream.setMainStream(map.get("MAIN"));
            }
            stream.sendString("OK");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getSizeOfMap(){
        return map.size();
    }

    public String getSessionFolder() {
        return "Session - " + getIdentifier();
    }

    public String getIdentifier() {
        return tempNetworkInformation.IP() + " - " + tempSystemInformation.USER_NAME();
    }

    public Streams getStreamByName(SocketType socketType){
        return map.get(socketType.toString());
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
}
