package Connections;

import GUI.Main;
import GUI.Server.ServerGUI;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@WebSocket
public class ServerV2 {

    ExecutorService executor = Executors.newFixedThreadPool(100); // 10 threads

    public ConcurrentHashMap<Session, Client> connectionsMap = new ConcurrentHashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println(message);
        System.out.println(Main.serverV2.connectionsMap.size());
        connectionsMap.get(session).processMessage(message);

    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        Client client = new Client(session);
        connectionsMap.put(session, client);
        System.out.println(connectionsMap.size());
        System.out.println("New connection from " + session.getRemoteAddress().getAddress().getHostAddress());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("action", "SYS_NET_INFO");
        session.getRemote().sendString(jsonObject.toString());
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        connectionsMap.remove(session);
        System.out.println("Closed connection to " + session.getRemoteAddress().getAddress().getHostAddress());
    }

    // Java (Servidor)

    @OnWebSocketMessage
    public void onMessage(Session session, byte[] buf, int offset, int length) {
        byte fileId = buf[offset];
        byte control = buf[offset + 1];
        Arrays.copyOfRange(buf, offset, offset + length);

/*
        // Inicializar el mapa para la sesión actual si no existe
        sessionToFileStreams.putIfAbsent(session, new ConcurrentHashMap<>());
        ConcurrentHashMap<Byte, FileOutputStream> fileStreams = sessionToFileStreams.get(session);

        // Continuar solo si fileStreams no es null
        if (fileStreams != null) {
            FileOutputStream fos;
            if (!fileStreams.containsKey(fileId)) {
                fos = createFileOutputStream(fileId);
                if (fos != null) {
                    fileStreams.put(fileId, fos);
                } else {
                    System.out.println("Could not create FileOutputStream for fileId: " + fileId);
                    return;
                }
            } else {
                fos = fileStreams.get(fileId);
            }

            if (fos != null) {
                try {
                    fos.write(buf, offset + 2, length - 2);
                    System.out.println("Readed: " + buf.length);

                    if (control == 0x02) {
                        System.out.println("Finally created! Found control byte!");
                        fos.flush();
                        fos.close();
                        fileStreams.remove(fileId);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("FileOutputStream not found for session and file id.");
            }
        } */
    }

    public void startServer() {
        org.eclipse.jetty.server.Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ServletHolder wsHolder = new ServletHolder("ws", new org.eclipse.jetty.websocket.servlet.WebSocketServlet() {
            @Override
            public void configure(org.eclipse.jetty.websocket.servlet.WebSocketServletFactory factory) {
                factory.register(ServerV2.class);
            }
        });
        context.addServlet(wsHolder, "/");

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConcurrentHashMap<Session, Client> getConnectionsMap() {
        return connectionsMap;
    }
}
