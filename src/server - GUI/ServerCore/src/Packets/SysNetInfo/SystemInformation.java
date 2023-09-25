package Packets.SysNetInfo;

import java.util.List;

public record SystemInformation(String OPERATING_SYSTEM, String USER_PROF, String HOME_PATH, String HOME_DRIVE,
                                String USER_NAME, List<String> USER_DISKS, String TAG_NAME, boolean WEBCAM,
                                boolean KEYLOGGER,
                                String UUID) {

}
