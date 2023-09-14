package Connections;

import GUI.Main;
import GUI.TableUtils.FileManager.FileManagerGUI;
import Information.NetworkInformation;
import Information.Response;
import Information.SystemInformation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.eclipse.jetty.websocket.api.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Client {
    private Session session;
    private SystemInformation sysInfo;
    private NetworkInformation netInfo;
    ExecutorService executor = Executors.newCachedThreadPool();
    public Updater updater;
    Map<Response, Consumer<JSONObject>> mapOfResponses = new HashMap<>();

    public Client(Session session) {
        this.session = session;
        updater = new Updater(this);
        setupMapOfResponses();
    }


    private void setupMapOfResponses() {
        mapOfResponses.put(Response.SYS_NET_INFO, updater::addRowOfNewConnection);
        mapOfResponses.put(Response.DISKS, updater::updateDisks);
        mapOfResponses.put(Response.DIRECTORY, updater::updateDirectory);
    }


    public void processMessage(String message) {

        JSONObject object = new JSONObject(message);
        Consumer<JSONObject> action = mapOfResponses.get(Response.valueOf(object.getString("RESPONSE")));
        if (action != null) {
            action.accept(object);
        } else {
            System.out.println("Action not found!");
        }
    }


    public void sendString(String message) throws IOException {
        session.getRemote().sendString(message);
    }
    public void processMessage(byte[] message) {

    }

    public String getUUID() {
        return sysInfo.UUID();
    }

    public String getIdentifier() {
        return netInfo.IP() + " - " + sysInfo.USER_NAME();
    }

    public void sendMessage(String message) throws IOException {
        session.getRemote().sendString(message);
    }

    public void setFileManagerGUI(FileManagerGUI fileManagerGUI) {
        updater.setFileManagerGUI(fileManagerGUI);
    }

    public void setSysInfo(SystemInformation sysInfo) {
        this.sysInfo = sysInfo;
    }

    public void setNetInfo(NetworkInformation netInfo) {
        this.netInfo = netInfo;
    }

    public SystemInformation getSysInfo() {
        return sysInfo;
    }

    public NetworkInformation getNetInfo() {
        return netInfo;
    }

    public ExecutorService getExecutor() {
        return executor;
    }


}
