package TableUtils.KeyboardController.Events;

import TableUtils.KeyboardController.KeyboardControllerGUI;
import Utilities.AbstractEventGUI;
import org.json.JSONObject;

import java.io.IOException;

public class KeyboardEvent extends AbstractEventGUI<KeyboardControllerGUI> {
    private final String command;

    public KeyboardEvent(KeyboardControllerGUI guiManager, String command) {
        super(guiManager);
        this.command = command;
    }

    @Override
    public void run() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "KEYBOARD_CONTROLLER");
        jsonObject.put("command", command);
        try {
            getClient().sendString(jsonObject.toString());
        } catch (IOException e) {
            handleGuiError();
        }
    }
}
