package TableUtils.KeyboardController.Events;

import TableUtils.KeyboardController.KeyboardControllerGUI;
import Utilities.Event.AbstractEventGUI;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class KeyboardEvent extends AbstractEventGUI<KeyboardControllerGUI> {
    private final JSONArray command;

    public KeyboardEvent(KeyboardControllerGUI guiManager, JSONArray command) {
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
