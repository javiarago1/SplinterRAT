package TableUtils.ReverseShell.Events;

import TableUtils.ReverseShell.ReverseShellGUI;
import Utilities.Event.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;

public class StartShellEvent extends AbstractEventGUI<ReverseShellGUI> {
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
            handleGuiError();
        }
    }
}
