package Utils;

import java.util.BitSet;

public class UniqueByteIDGenerator {
    private final BitSet elements = new BitSet(256);

    public synchronized byte getID() {
        int i = elements.nextClearBit(0);

        if (i < 0 || i > 255) {
            throw new IllegalStateException("No more IDs available");
        }

        elements.set(i, true);
        return (byte) i;
    }

    public synchronized void finishTask(byte id) {
        if (id < 0) {
            throw new IllegalArgumentException("Invalid ID: " + id);
        }

        elements.set(id, false);
    }
}
