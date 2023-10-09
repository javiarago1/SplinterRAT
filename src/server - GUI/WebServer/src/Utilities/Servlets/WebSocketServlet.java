package Utilities.Servlets;


import Utilities.WebSocketClient;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class WebSocketServlet extends org.eclipse.jetty.websocket.servlet.WebSocketServlet {
    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.getPolicy().setMaxTextMessageSize(204800);
        webSocketServletFactory.getPolicy().setMaxBinaryMessageSize(204800);
        webSocketServletFactory.register(WebSocketClient.class);
    }
}
