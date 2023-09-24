package Server;

import Updater.UpdaterInterface;
import org.eclipse.jetty.websocket.api.Session;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionStore {

    public static UpdaterInterface updaterInterface;

    public static void setUpdaterInterface(UpdaterInterface updaterInterface) {
        ConnectionStore.updaterInterface = updaterInterface;
    }

    public static final ConcurrentHashMap<Session, Client> connectionsMap = new ConcurrentHashMap<>();

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
