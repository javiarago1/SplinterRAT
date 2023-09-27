package TableUtils.ReverseShell.Actions;

import TableUtils.ReverseShell.Events.CommandEvent;
import TableUtils.ReverseShell.ReverseShellGUI;
import Utilities.Action.AbstractActionGUI;

import java.awt.event.ActionEvent;

public class SendCommandAction extends AbstractActionGUI<ReverseShellGUI> {

    public SendCommandAction(ReverseShellGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!getGUIManager().getFieldOfCommands().getText().isEmpty()) {
            String command = getGUIManager().getFieldOfCommands().getText();
            getGUIManager().getFieldOfCommands().setText(""); // set field text empty
            getClient().getExecutor().submit(new CommandEvent(getGUIManager(), command));
        }
    }
}
