package Connections;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BytesChannel {
    private final byte id;
    private ByteBuffer buffer;

    private Category category;

    private String categoryOutputFolder;

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;

    public BytesChannel(byte id, Category category) {
        this.category = category;
        this.id = id;
        this.buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        setOutputFolder(category);
    }


    public BytesChannel(byte id) {
        this.id = id;
        this.buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
    }

    private void setOutputFolder(Category category) {
        switch (category) {
            case ZIP_FILE -> {
                categoryOutputFolder = "Downloaded files";
            }
            case KEYLOGGER_LOGS -> {

            }
            case WEBCAM_LOGS -> {
                categoryOutputFolder = "Webcam logs";
            }
        }
    }

    public ByteBuffer getBuffer() {
        return buffer;
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
        if (buffer.remaining() < length) {
            ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
            buffer.flip();
            newBuffer.put(buffer);
            buffer = newBuffer;
        }
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

    public Category getCategory() {
        return category;
    }
}

