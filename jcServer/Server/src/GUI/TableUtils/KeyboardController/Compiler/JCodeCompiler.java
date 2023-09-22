package GUI.TableUtils.KeyboardController.Compiler;


import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * This compiler is used to transform the JList into an interpretable String for c++ runner
 */
public class JCodeCompiler {
    public static String compile(DefaultListModel<String> listOfEventsModel) {
        StringBuilder interpretableString = new StringBuilder();
        for (int i = 0; i < listOfEventsModel.size(); i++) {
            String element = listOfEventsModel.getElementAt(i);
            Pattern delayPattern = Pattern.compile("^(Delay: )\\d+ ms$");
            String startTextOfArea = "Text to write: ";
            String startSpecialKey = "Special key: ";
            if (delayPattern.matcher(element).find()) {
                Pattern numberPattern = Pattern.compile("\\d+");
                Matcher numberMatcher = numberPattern.matcher(element);
                if (numberMatcher.find()) interpretableString.append("jcDelay/").append(numberMatcher.group());
            } else if (element.startsWith(startTextOfArea)) {
                interpretableString.append(element.substring(startTextOfArea.length()));
            } else {
                int specialKey = getIntOfSpecialKey(element.substring(startSpecialKey.length()));
                interpretableString.append("jcOrder/").append(specialKey);
            }
            if (i != listOfEventsModel.size() - 1) interpretableString.append("|");
        }
        return interpretableString.toString();
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
