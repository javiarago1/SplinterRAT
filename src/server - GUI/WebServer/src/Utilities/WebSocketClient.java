package Utilities;

import Packets.SysNetInfo.Information;
import Server.Client;
import Server.ConnectionStore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.eclipse.jetty.util.ajax.JSON;
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
        Client client = new Client(session);
        ConnectionStore.addToWebSessionMap(session, client);
        ObjectMapper mapper = new ObjectMapper();

        // Create the root JSON object
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("RESPONSE", "TABLE_INFO");

        // Create the 'content' object that will store the Information objects using UUID as key
        ObjectNode contentObject = mapper.createObjectNode();

        for (Client clientInstance : ConnectionStore.connectionsMap.values()) {
            Information information = clientInstance.updater.getInformation();

            // Use UUID as the key for each client's information
            contentObject.set(information.systemInformation().UUID(), mapper.valueToTree(information));
        }

        // Add the 'content' object to the root object
        rootNode.set("content", contentObject);

        try {
            String result = mapper.writeValueAsString(rootNode);
            session.getRemote().sendString(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("New connection from " + session.getRemoteAddress().getAddress().getHostAddress());
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        JSONObject object = new JSONObject(message);
        String clientId = object.getString("client_id");
        System.out.println(message);
        if (object.getString("ACTION").equals("SELECT_CLIENT")){
            Client mainClient = ConnectionStore.getConnection(clientId);
            WebUpdater webUpdater = (WebUpdater) mainClient.updater;
            if (object.getBoolean("set_null")){
                System.out.println("Let's set to null the selected client");
                webUpdater.setCurrentClient(null);
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("RESPONSE","CLIENT_SET");
                jsonObject.put("client_id", clientId);
                webUpdater.setCurrentClient(new Client(session));
                try {
                    session.getRemote().sendString(jsonObject.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            Client windowsClient = ConnectionStore.getConnection(clientId);
            try {
                windowsClient.sendString(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }




    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {

    }


}
