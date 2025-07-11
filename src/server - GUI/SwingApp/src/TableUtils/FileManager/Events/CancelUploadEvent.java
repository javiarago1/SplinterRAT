package TableUtils.FileManager.Events;

import ProgressBar.UploadProgressBar;
import Utilities.Event.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class CancelUploadEvent extends AbstractEventGUI<UploadProgressBar<?>> {
    private final AtomicBoolean cancellationAtomic;

    public CancelUploadEvent(UploadProgressBar<?> uploadProgressBar, AtomicBoolean cancellationAtomic) {
        super(uploadProgressBar);
        this.cancellationAtomic = cancellationAtomic;
    }

    @Override
    public void run() {
        cancellationAtomic.set(false);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "CANCEL_UPLOAD");
        jsonObject.put("channel_id", getGUIManager().getId());
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
           handleGuiError();
        }
    }
}
