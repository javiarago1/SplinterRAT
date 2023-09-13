package Connections;

import GUI.TableUtils.FileManager.Movement;
import Information.Action;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sender {
    private final Session session;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final RemoteEndpoint remote;

    public Sender(Session session) {
        this.session = session;
        remote = session.getRemote();
    }

    public void requestDisks() {
        executor.submit(() -> {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ACTION", "DISKS");
                remote.sendString(jsonObject.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void requestDirectory(final String path) {
        executor.submit(() -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ACTION", "DIRECTORY");
            jsonObject.put("path", path);
            try {
                remote.sendString(jsonObject.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


}

