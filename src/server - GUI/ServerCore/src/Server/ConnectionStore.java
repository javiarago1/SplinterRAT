package Server;

import Updater.UpdaterFactory;
import Updater.UpdaterInterface;
import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionStore {

    public static UpdaterFactory updaterFactory;

    public static final ConcurrentHashMap<String, Client> connectionsMap = new ConcurrentHashMap<>();

    // duplicated above just for normal server

    public static final ConcurrentHashMap<Session, Client> webSessionsMap = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Client> webClientsMap = new ConcurrentHashMap<>();

    public static void addToWebSessionMap(Session session, Client client){
        webSessionsMap.put(session, client);
    }

    public static Client getWebConnectionIdentifiedByUUID(String uuid){
        return webClientsMap.get(uuid);
    }


    public static void addConnectionToWebClientMap(String uuid, Client client){
        webClientsMap.put(uuid, client);
    }

    public static void addConnection(String session, Client client) {
        connectionsMap.put(session, client);
    }

    public static Client getConnection(String session) {
        return connectionsMap.get(session);
    }

    public static void removeConnection(String session) {
        connectionsMap.remove(session);
    }

    public static int getNumOfConnectedUsers() {
        return connectionsMap.size();
    }


    public static void removeConnectionWeb(Session session) {
        webSessionsMap.remove(session);
    }
}
