package Connections;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler {
    private Streams mainStream;
    private Streams fileManagerStream;

    public ClientHandler(Socket socket) {
        addStream(socket);
    }

    public Streams addStream(Socket socket) {
        Streams stream;
        String nameOfOperation;
        try {
            stream = new Streams(socket);
            stream.sendString("WAU");
            nameOfOperation = stream.readString();
            switch (nameOfOperation) {
                case "MAIN" -> mainStream = stream;
                case "FILE_MANAGER" -> fileManagerStream = stream;
            }
            stream.sendString("OK");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return stream;
    }

    public Streams getMainStream() {
        return mainStream;
    }

    public Streams getFileManagerStream() {
        return fileManagerStream;
    }

}
