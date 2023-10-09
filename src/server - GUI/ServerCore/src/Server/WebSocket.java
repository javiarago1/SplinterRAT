package Server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;



@org.eclipse.jetty.websocket.api.annotations.WebSocket
@SuppressWarnings("unused")
public class WebSocket {


    @OnWebSocketError
    public void handleError(Session session, Throwable throwable) {
        if (throwable instanceof org.eclipse.jetty.io.EofException
                || (throwable.getCause() != null
                && throwable.getCause() instanceof java.io.IOException
                && "Connection reset by peer".equals(throwable.getCause().getMessage()))) {
            // Handle the abrupt disconnection here
            String clientId = session.getUpgradeRequest().getHeader("Client-ID");
            ConnectionStore.removeConnection(clientId);
            System.out.println("Client disconnected abruptly.");
        } else {
            // Handle other errors
            System.out.println("An error occurred: " + throwable.getMessage());
        }
    }

    // ... Ot

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        String clientId = session.getUpgradeRequest().getHeader("Client-ID");
        System.out.println(message);
        ConnectionStore.getConnection(clientId).updater.processMessage(message);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        String clientId = session.getUpgradeRequest().getHeader("Client-ID");
        Client client = new Client(session);

        ConnectionStore.addConnection(clientId, client);

        System.out.println("New connection from " + session.getRemoteAddress().getAddress().getHostAddress());
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        String clientId = session.getUpgradeRequest().getHeader("Client-ID");
        ConnectionStore.removeConnection(clientId);
        System.out.println("Closed connection to " + clientId);
    }

    // Java (Servidor)

    // Global variable

    @OnWebSocketMessage
    public void onMessage(Session session, byte[] buf, int offset, int length) {
        String clientId = session.getUpgradeRequest().getHeader("Client-ID");
        Client client = ConnectionStore.getConnection(clientId);

        ConcurrentHashMap<Byte, BytesChannel> sessionToFileChannels = client.getFileChannels();

        byte fileId = buf[offset];
        byte control = buf[offset + 1];

        BytesChannel bytesChannel = sessionToFileChannels.get(fileId);
        if (bytesChannel == null) {
            return;
        }

        byte[] finalData = bytesChannel.handleMessage(buf, offset, length, control);

        if (finalData != null) {

            client.handleFileCompletion(bytesChannel, finalData);
        }
    }



}
