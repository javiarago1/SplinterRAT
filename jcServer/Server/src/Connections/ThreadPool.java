package Connections;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool extends Thread {
    private final ServerSocket server;
    private final ConcurrentHashMap<String, ClientHandler> dialog;
    private final ExecutorService executor = Executors.newFixedThreadPool(30);

    public ThreadPool(ServerSocket server, ConcurrentHashMap<String, ClientHandler> dialog) {
        if (server == null || dialog == null) throw new IllegalArgumentException();
        this.server = server;
        this.dialog = dialog;
    }

    public void run(){
        if (server == null || dialog == null || executor == null)
            throw new IllegalArgumentException("Invalid arguments!");
        for (int i = 0; i < 30; i++)
            executor.submit(new Connection(server, executor, dialog));
    }

    public void shutdownNow() {
        executor.shutdownNow();
    }

}