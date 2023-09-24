package Updater;

import Information.NetworkInformation;
import Information.SystemInformation;
import org.json.JSONObject;

public interface UpdaterInterface {
    void addRowOfNewConnection(JSONObject jsonObject);
    void updateDisks(JSONObject jsonObject);
    void updateDirectory(JSONObject jsonObject);
    void updateWebcamDevices(JSONObject jsonObject);
    void updateFrameOfWebcamStreamer(byte[] data);
    void updateReverseShell(JSONObject jsonObject);
    void updateFrameOfScreenStreamer(byte[] finalData);
    void setScreenDimensions(JSONObject jsonObject);
    void updateMonitors(JSONObject jsonObject);
    void updateCredentialsDumper(String path);
    void showPermissionStatus(JSONObject jsonObject);
    SystemInformation getSystemInformation();
    NetworkInformation getNetworkInformation();

}
