package Connections;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool extends Thread {
    private final ServerSocket server;
    private final ConcurrentHashMap<String, ClientHandler> dialog;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public ThreadPool(ServerSocket server, ConcurrentHashMap<String, ClientHandler> dialog) {
        if (server == null || dialog == null) throw new IllegalArgumentException();
        this.server = server;
        this.dialog = dialog;
    }

    public void run() {
        if (server == null || dialog == null || executor == null)
            throw new IllegalArgumentException("Invalid arguments!");
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket socket = server.accept();
                executor.submit(new Connection(socket, dialog));
            } catch (Exception e) {
                // Maneja la excepción adecuadamente aquí
            }
        }
    }

    public void shutdownNow() {
        executor.shutdownNow();
    }
}
