package Updater;

import Packets.SysNetInfo.Information;
import Packets.SysNetInfo.NetworkInformation;
import Packets.SysNetInfo.SystemInformation;
import org.json.JSONObject;

public interface UpdaterInterface {
    void processMessage(String message);
    void updateFrameOfWebcamStreamer(byte[] data);
    void updateFrameOfScreenStreamer(byte[] finalData);
    void updateDownloadState(byte id, int read, boolean isLastPacket);
    Information getInformation();
    SystemInformation getSystemInformation();
    NetworkInformation getNetworkInformation();

}
