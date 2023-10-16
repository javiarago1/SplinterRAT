package Server;

import Packets.Servlet.ServletConfiguration;
import Packets.Servlet.ServletPacket;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;

import java.util.EnumSet;
import java.util.List;

public class Server {
    private int port;
    private org.eclipse.jetty.server.Server server;

    public Server(int port) {
        this.port = port;
        configureServer(null);
    }

    public Server(int port, ServletConfiguration servletConfiguration) {
        this.port = port;
        configureServer(servletConfiguration);
    }


    private void configureServer(ServletConfiguration servletConfiguration) {
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

        if (servletConfiguration == null) return;

        List<ServletPacket> servletPackets = servletConfiguration.getServletPackets();
        for(ServletPacket e: servletPackets){
            context.addServlet(e.servletHolder(), e.pathSpec());
        }
        FilterHolder filterHolder = servletConfiguration.getFilterHolder();
        context.addFilter(filterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));
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
