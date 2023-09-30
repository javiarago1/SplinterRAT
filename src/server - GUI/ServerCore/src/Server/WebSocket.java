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
            ConnectionStore.removeConnection(session);
            System.out.println("Client disconnected abruptly.");
        } else {
            // Handle other errors
            System.out.println("An error occurred: " + throwable.getMessage());
        }
    }

    // ... Ot

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
        ConnectionStore.getConnection(session).updater.processMessage(message);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        Client client = new Client(session);
        String clientId = session.getUpgradeRequest().getHeader("Client-ID");
        ConnectionStore.addConnection(session, client);
        ConnectionStore.addConnectionWithUUIDIdentifier(clientId, client);

        System.out.println("New connection from " + session.getRemoteAddress().getAddress().getHostAddress());
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        ConnectionStore.removeConnection(session);
        System.out.println("Closed connection to " + session.getRemoteAddress().getAddress().getHostAddress());
    }

    // Java (Servidor)

    // Global variable

    @OnWebSocketMessage
    public void onMessage(Session session, byte[] buf, int offset, int length) {
        Client client = ConnectionStore.getConnection(session);
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
