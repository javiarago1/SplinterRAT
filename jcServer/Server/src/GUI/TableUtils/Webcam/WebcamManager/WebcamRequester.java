package GUI.TableUtils.Webcam.WebcamManager;

import Connections.Client;
import Connections.ClientErrorHandler;
import Information.Action;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;
import java.util.List;


/*
 * Requesting webcam devices through sockets and getting
 * a list containing them.
 */

public class WebcamRequester implements Runnable {
    private final Client client;

    public WebcamRequester(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "WEBCAM_DEVICES");
        try {
            client.sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
