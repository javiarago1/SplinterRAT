package Server;

import Packets.SysNetInfo.Information;
import Server.Client;
import Server.ConnectionStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;


@WebSocket

public class WebSocketClient {


    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        // Client client = new Client(session);
        Client client = new Client(session);
        ConnectionStore.addToWebSessionMap(session, client);
        ConcurrentHashMap<Session, Client> tempMap = ConnectionStore.connectionsMap;
        ObjectMapper mapper = new ObjectMapper();

        // Create the root JSON object
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("RESPONSE", "TABLE_INFO");

        // Create the 'content' array that will store the Information objects
        ArrayNode contentArray = mapper.createArrayNode();

        for (Client clientInstance : ConnectionStore.connectionsMap.values()) {
            Information information = clientInstance.updater.getInformation();
            contentArray.add(mapper.valueToTree(information));
        }

        // Add the 'content' array to the root object
        rootNode.set("content", contentArray);

        try {
            String result = mapper.writeValueAsString(rootNode);
            session.getRemote().sendString(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("New connection froma " + session.getRemoteAddress().getAddress().getHostAddress());
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        JSONObject object = new JSONObject(message);
        System.out.println("Action"+object);
        String clientId = object.getString("client_id");
        Client webClient = ConnectionStore.getWebConnectionIdentifiedByUUID(clientId);
        if (webClient == null) {
            Client temp2 = new Client(session);
            ConnectionStore.addConnectionToWebClientMap(clientId, temp2);
            ConnectionStore.webSessionsMap.get(session).updater = ConnectionStore.connectionsMapIdentifiedByUUID.get(clientId).updater;
            System.out.println("Client doesn't exists so i create it in map");
        } else System.out.println("Client already exists");
        Client windowsClient = ConnectionStore.getWindowsConnectionByIdentifier(clientId);
        try {
            windowsClient.sendString(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Client tempClient = ConnectionStore.webSessionsMap.get(session);
        String uuid = tempClient.updater.getSystemInformation().UUID();
        ConnectionStore.removeConnection(session);
        ConnectionStore.webClientsMap.remove(uuid);
        System.out.println(ConnectionStore.webClientsMap);
        System.out.println("Closed connection toa " + session.getRemoteAddress().getAddress().getHostAddress());
    }


}
