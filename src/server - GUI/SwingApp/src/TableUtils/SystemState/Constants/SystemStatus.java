package TableUtils.SystemState.Constants;

public enum SystemStatus {
    LOG_OFF(0),
    SHUTDOWN(1),
    REBOOT(2);

    private final int value;

    SystemStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
