package GUI.Compiler;

import java.util.UUID;

public class Mutex {
    public static String generateMutex() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
