package Utilities;

import Packets.Identificators.Response;
import Packets.SysNetInfo.Information;
import Packets.SysNetInfo.NetworkInformation;
import Packets.SysNetInfo.SystemInformation;
import Server.Client;
import Server.ConnectionStore;
import Updater.UpdaterInterface;
import Utils.Converter;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class WebUpdater implements UpdaterInterface {

    private final Map<Response, Consumer<JSONObject>> mapOfResponses = new HashMap<>();
    private SystemInformation systemInformation;

    private NetworkInformation networkInformation;

    private Information information;

    private void addRowOfNewConnection(JSONObject jsonObject) {
        information = Converter.convertJSON2NetAndSysInfo(jsonObject);
        systemInformation = information.systemInformation();
        networkInformation = information.networkInformation();
    }

    public void setupMapOfResponses() {
        mapOfResponses.put(Response.SYS_NET_INFO, this::addRowOfNewConnection);
    }

    @Override
    public void processMessage(String message) {
        System.out.println("Happened! " + message);
        JSONObject object = new JSONObject(message);
        /*if (object.getString("RESPONSE").equals("SYS_NET_INFO")){
            for (Session e: ConnectionStore.webSessionsMap.keySet()){
                System.out.println("Sending row to web client");
                try {
                    if (e.isOpen())e.getRemote().sendString(message);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }*/
        String clientId = object.getString("client_id");
        Client webClient = ConnectionStore.getWebConnectionIdentifiedByUUID(clientId);
        Consumer<JSONObject> action = mapOfResponses.get(Response.valueOf(object.getString("RESPONSE")));
        if (action != null) {
            action.accept(object);
        }
        if (webClient == null) {
            return;
        }
        try {
            webClient.sendString(message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public boolean shouldExtract() {
        return false;
    }

    public WebUpdater() {
        setupMapOfResponses();
    }

    @Override
    public void updateFrameOfWebcamStreamer(byte[] data) {

    }

    @Override
    public void updateFrameOfScreenStreamer(byte[] finalData) {

    }

    @Override
    public void updateDownloadState(byte id, int read, boolean isLastPacket) {

    }

    @Override
    public Information getInformation() {
        return information;
    }

    @Override
    public SystemInformation getSystemInformation() {
        return systemInformation;
    }

    @Override
    public NetworkInformation getNetworkInformation() {
        return networkInformation;
    }
}
