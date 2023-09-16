package Connections;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BytesChannel {
    private final byte id;
    private final ByteBuffer buffer;

    private String categoryOutputFolder;

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;

    public BytesChannel(byte id, String categoryOutputFolder) {
        this.categoryOutputFolder = categoryOutputFolder;
        this.id = id;
        this.buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
    }

    public BytesChannel(byte id) {
        this.id = id;
        this.buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
    }

    public byte getId() {
        return id;
    }


    public byte[] handleMessage(byte[] buf, int offset, int length, byte control) {
        write(buf, offset + 2, length - 2);
        if (control == 0x02) {
            return flipAndGet();
        }
        return null;
    }


    private void write(byte[] data, int offset, int length) {
        buffer.put(Arrays.copyOfRange(data, offset, offset + length));
    }


    private byte[] flipAndGet() {
        buffer.flip();
        byte[] finalData = new byte[buffer.remaining()];
        buffer.get(finalData);
        return finalData;
    }

    public String getCategoryOutputFolder() {
        return categoryOutputFolder;
    }
}

