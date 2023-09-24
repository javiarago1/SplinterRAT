package TableUtils.ReverseShell.Actions;

import TableUtils.ReverseShell.Events.CommadEvent;
import TableUtils.ReverseShell.ReverseShellGUI;
import Utilities.AbstractAction;

import java.awt.event.ActionEvent;

public class SendCommandAction extends AbstractAction<ReverseShellGUI> {

    public SendCommandAction(ReverseShellGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!getGUIManager().getFieldOfCommands().getText().isEmpty()) {
            String command = getGUIManager().getFieldOfCommands().getText();
            getGUIManager().getFieldOfCommands().setText(""); // set field text empty
            getClient().getExecutor().submit(new CommadEvent(getGUIManager(), command));
        }
    }
}
