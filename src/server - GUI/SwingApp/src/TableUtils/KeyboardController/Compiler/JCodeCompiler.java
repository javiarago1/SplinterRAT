package TableUtils.KeyboardController.Compiler;


import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This compiler is used to transform the JList into an interpretable String for c++ runner
 */
public class JCodeCompiler {
    public static JSONArray compile(DefaultListModel<String> listOfEventsModel) {
        JSONArray jsonEvents = new JSONArray();
        for (int i = 0; i < listOfEventsModel.size(); i++) {
            String element = listOfEventsModel.getElementAt(i);
            JSONObject eventJson = new JSONObject();

            Pattern delayPattern = Pattern.compile("^(Delay: )\\d+ ms$");
            String startTextOfArea = "Text to write: ";
            String startSpecialKey = "Special key: ";

            if (delayPattern.matcher(element).find()) {
                Pattern numberPattern = Pattern.compile("\\d+");
                Matcher numberMatcher = numberPattern.matcher(element);
                if (numberMatcher.find()) {
                    eventJson.put("type", "jcDelay");
                    eventJson.put("value", Integer.parseInt(numberMatcher.group()));
                }
            }  else if (element.startsWith(startTextOfArea)) {
                eventJson.put("type", "text");
                eventJson.put("value", element.substring(startTextOfArea.length()));
            } else {
                eventJson.put("type", "jcOrder");
                eventJson.put("value", getIntOfSpecialKey(element.substring(startSpecialKey.length())));
            }

            jsonEvents.put(eventJson);
        }

        return jsonEvents;
    }


    private static int getIntOfSpecialKey(String order) {
        switch (order) {
            case "Enter key" -> {
                return 13;
            }
            case "Left Windows key" -> {
                return 91;
            }
            case "Tab key" -> {
                return 9;
            }
            default -> {
                return 0;
            }
        }
    }

}
