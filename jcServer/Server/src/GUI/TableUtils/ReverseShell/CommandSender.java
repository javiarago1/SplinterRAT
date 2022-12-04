package GUI.TableUtils.ReverseShell;

import Information.Action;

import javax.swing.*;
import java.io.IOException;


public class CommandSender extends SwingWorker<Void, Void> {
    private final ReverseShellGUI reverseShellGUI;
    private final String command;

    private final String newLine;

    public CommandSender(ReverseShellGUI reverseShellGUI) {
        this.reverseShellGUI = reverseShellGUI;
        command = reverseShellGUI.getFieldOfCommands().getText();
        newLine = "\n";

    }

    public CommandSender(ReverseShellGUI reverseShellGUI, String customCommand) {
        this.reverseShellGUI = reverseShellGUI;
        command = customCommand;
        newLine = ""; // first occurrence of reverse shell

    }

    private String response = "";

    @Override
    protected Void doInBackground() throws IOException {
        String receivedResult = reverseShellGUI.getStream().sendAndReadAction(Shell.COMMAND, command); // raw
        String result = "";
        String path;
        String[] parts = receivedResult.split("\\|"); // separate by result of command and current path
        if (parts.length == 0) {
            path = reverseShellGUI.getLastPath(); // use last path saved if command was wrong
        } else {
            result = parts[0];
            path = parts[1];
            reverseShellGUI.setLastPath(path); // save current path
        }


        response = result + "\n" + path + ">"; // result concatenated
        return null;
    }

    @Override
    protected void done() {
        reverseShellGUI.getTextAreaOfResult().append(newLine + response);
    }
}
