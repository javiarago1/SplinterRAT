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
        command = reverseShellGUI.getTextAreaOfResult().getText();

    }

    String userConsolePosition = "";
    String resultOfCommand = "";
    String response = "";

    @Override
    protected Void doInBackground() throws IOException {
        System.out.println(command);
        response = reverseShellGUI.getStream().sendAndReadJSONX(Action.SHELL_COMMAND, command);
        return null;
    }

    @Override
    protected void done() {
        String totalResult = response;
        reverseShellGUI.getTextAreaOfResult().append(totalResult);
    }
}
