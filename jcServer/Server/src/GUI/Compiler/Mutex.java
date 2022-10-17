package GUI.Compiler;

import java.util.UUID;

public class Mutex {
    // Generate unique mutex for Windows process identification
    public static String generateMutex() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
