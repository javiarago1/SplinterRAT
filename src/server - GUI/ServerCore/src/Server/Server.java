package Server;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Server {
    private int port;
    private boolean isWebServer;
    private org.eclipse.jetty.server.Server server;
    public Server(int port) {
        this.port = port;
        configureServer();
    }

    public Server(int port, boolean isWebServer) {
        this.port = port;
        this.isWebServer = isWebServer;
        configureServer();
    }

    private void configureServer(){
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

                factory.register(WebSocket.class);
            }
        });
        context.addServlet(wsHolder, "/");

        if (isWebServer) {
            wsHolder = new ServletHolder("ws2", new org.eclipse.jetty.websocket.servlet.WebSocketServlet() {
                @Override
                public void configure(org.eclipse.jetty.websocket.servlet.WebSocketServletFactory factory) {
                    // Configura el tamaño máximo del mensaje (en bytes).
                    factory.getPolicy().setMaxTextMessageSize(204800);
                    factory.getPolicy().setMaxBinaryMessageSize(204800);

                    factory.register(WebSocketClient.class);
                }
            });
            context.addServlet(wsHolder, "/web");
        }


    }

    public void startServer() {


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
