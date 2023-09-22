package GUI.TableUtils.ReverseShell.Actions;

import GUI.TableUtils.ReverseShell.Events.CommadEvent;
import GUI.TableUtils.ReverseShell.ReverseShellGUI;
import Information.AbstractAction;

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
