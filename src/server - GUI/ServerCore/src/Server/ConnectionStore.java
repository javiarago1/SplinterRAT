package Server;

import Updater.UpdaterFactory;
import Updater.UpdaterInterface;
import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionStore {

    public static UpdaterFactory updaterFactory;

    public static final ConcurrentHashMap<Session, Client> connectionsMap = new ConcurrentHashMap<>();

    public static final ConcurrentHashMap<String, Client> connectionsMapIdentifiedByUUID = new ConcurrentHashMap<>();

    // duplicated above just for normal server

    public static final ConcurrentHashMap<Session, Client> webSessionsMap = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Client> webClientsMap = new ConcurrentHashMap<>();

    public static void addToWebSessionMap(Session session, Client client){
        webSessionsMap.put(session, client);
    }

    public static Client getWebConnectionIdentifiedByUUID(String uuid){
        return webClientsMap.get(uuid);
    }

    public static Client getWindowsConnectionByIdentifier(String uuid){
        return connectionsMapIdentifiedByUUID.get(uuid);
    }

    public static void addConnectionWithUUIDIdentifier(String uuid, Client client){
        connectionsMapIdentifiedByUUID.put(uuid, client);
    }
    public static void addConnectionToWebClientMap(String uuid, Client client){
        webClientsMap.put(uuid, client);
    }

    public static void addConnection(Session session, Client client) {
        connectionsMap.put(session, client);
    }

    public static Client getConnection(Session session) {
        return connectionsMap.get(session);
    }

    public static void removeConnection(Session session) {
        connectionsMap.remove(session);
    }

    public static int getNumOfConnectedUsers() {
        return connectionsMap.size();
    }



}
