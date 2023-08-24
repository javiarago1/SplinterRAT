package Connections;

import GUI.TableUtils.Configuration.SocketType;
import Information.NetworkInformation;
import Information.SystemInformation;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;


public class ClientHandler {
    private final ConcurrentHashMap<SocketType, Streams> map = new ConcurrentHashMap<>();
    private SystemInformation tempSystemInformation;
    private NetworkInformation tempNetworkInformation;

    private Streams mainStream;

    public void addStream(Identifier identifier) {

        map.put(identifier.socketType(), identifier.stream());
        if (identifier.socketType() == SocketType.MAIN) {
            identifier.stream().setMainStream(identifier.stream());
            mainStream = identifier.stream();
        } else {
            identifier.stream().setMainStream(mainStream);
        }


    }

    public int getSizeOfMap() {
        return map.size();
    }

    public String getSessionFolder() {
        return "Session - " + getIdentifier();
    }

    public String getIdentifier() {
        return tempNetworkInformation.IP() + " - " + tempSystemInformation.USER_NAME();
    }

    public Streams getStreamByName(SocketType socketType) {
        return map.get(socketType);
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
