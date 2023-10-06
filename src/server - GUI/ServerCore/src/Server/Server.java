package Server;

import Packets.Identificators.Category;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.JSONObject;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Scanner;

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

    public static class MyHttpServlet extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException, IOException {
            // Parse JSON request
            Scanner scanner = new Scanner(req.getInputStream()).useDelimiter("\\A");
            String body = scanner.hasNext() ? scanner.next() : "";
            JSONObject jsonRequest = new JSONObject(body);

            // Log to console
            System.out.println("Received JSON: " + jsonRequest.toString());

            String clientId = jsonRequest.getString("client_id");

            Client client = ConnectionStore.getWindowsConnectionByIdentifier(clientId);
            BytesChannel bytesChannel = client.createFileChannel(Category.ZIP_FILE);

            // Create JSON response
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("channel_id", bytesChannel.getId());

            // Send response
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(jsonResponse);
        }
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

        // TODO PLEASE FIX THIS (T＿T)
        FilterHolder cors = new FilterHolder(CrossOriginFilter.class);
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,POST,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
        context.addFilter(cors, "/*", EnumSet.of(DispatcherType.REQUEST));
        context.addServlet(new ServletHolder(new MyHttpServlet()), "/create-byte-channel");


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
