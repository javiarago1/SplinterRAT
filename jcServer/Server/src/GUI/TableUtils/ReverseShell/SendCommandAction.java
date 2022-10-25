package GUI.TableUtils.ReverseShell;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendCommandAction implements ActionListener {
    private final ReverseShellGUI reverseShellGUI;

    public SendCommandAction(ReverseShellGUI reverseShellGUI) {
        this.reverseShellGUI = reverseShellGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        reverseShellGUI.getStream().getExecutor().submit(new CommandSender(reverseShellGUI));
        reverseShellGUI.getFieldOfCommands().setText("");
    }
}
