package Utilities;

import Packets.Identificators.Response;
import Packets.SysNetInfo.Information;
import Packets.SysNetInfo.NetworkInformation;
import Packets.SysNetInfo.SystemInformation;
import Server.Client;
import Server.ConnectionStore;
import Updater.UpdaterInterface;
import Utils.Converter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

    // TODO FIX THIS
    @Override
    public void processMessage(String message) {
        System.out.println("Happened! " + message);
        JSONObject object = new JSONObject(message);
        if (object.getString("RESPONSE").equals("SYS_NET_INFO")) {
            Information information = Converter.convertJSON2NetAndSysInfo(object);
            ObjectMapper mapper = new ObjectMapper();

            try {
                ObjectNode infoNode = mapper.valueToTree(information);
                infoNode.put("RESPONSE", "SYS_NET_INFO");
                String result = mapper.writeValueAsString(infoNode);
                for (Session e: ConnectionStore.webSessionsMap.keySet()){
                    System.out.println("Sending row to web client");
                    try {
                        if (e.isOpen()) {
                            e.getRemote().sendString(result);
                            System.out.println(result);
                            System.out.println("Found opened");
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

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
