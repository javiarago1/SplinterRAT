package Connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private final ServerSocket server;
    private final ConcurrentHashMap <Socket, Streams> dialog = new ConcurrentHashMap <>();
    private final ThreadPool tp;
    private boolean running = false;
    private final int port;

    public Server(int port) throws IOException{
        this.port = port;
        server = new ServerSocket(port);
        tp = new ThreadPool(server, dialog);
    }

    public boolean isRunning(){
        return running;
    }

    public void startServer(){
        if (isRunning()) throw new IllegalStateException("Server already running");
        running = true;
        System.out.println("Server started!");
        tp.start();
    }

    public void stopServer() throws IOException{
        if (!isRunning()) throw new IllegalStateException("Server already idle");
        running = false;
        server.close();
        System.out.println("Server closed!");
        tp.shutdownNow();
    }

    public ConcurrentHashMap<Socket, Streams> getMap() {
        return dialog;
    }

    public int getPort() {
        return port;
    }
}