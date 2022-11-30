package Connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private ServerSocket server;
    private final ConcurrentHashMap<Socket, Streams> dialog = new ConcurrentHashMap<>();
    private ThreadPool tp;
    private boolean running = false;

    private int port;

    public boolean isRunning() {
        return running;
    }

    public Server(int port) {
        this.port = port;
    }

    public void startServer() throws IOException {
        if (isRunning()) throw new IllegalStateException("Server already running");
        server = new ServerSocket(port);
        tp = new ThreadPool(server, dialog);
        running = true;
        tp.start();
        System.out.println("Server started!");
    }

    public void definePort(int port) throws IOException {
        stopServer();
        this.port = port;
        startServer();
    }

    public void stopServer() throws IOException {
        if (!isRunning()) throw new IllegalStateException("Server already idle");
        running = false;
        server.close();
        stopStream();
        dialog.clear();
        tp.shutdownNow();
        System.out.println("Server closed!");
    }

    public void stopStream() {
        for (Socket key : dialog.keySet()) {
            try {
                dialog.get(key).getClientSocket().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ConcurrentHashMap<Socket, Streams> getMap() {
        return dialog;
    }

    public int getPort() {
        return port;
    }
}