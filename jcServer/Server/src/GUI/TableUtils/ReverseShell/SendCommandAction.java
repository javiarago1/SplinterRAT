package GUI.TableUtils.ReverseShell;

import Information.AbstractAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendCommandAction extends AbstractAction<ReverseShellGUI> {

    public SendCommandAction(ReverseShellGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!getGUIManager().getFieldOfCommands().getText().isEmpty()) {
            getGUIManager().setPressedEnter(true);
            String command = getGUIManager().getFieldOfCommands().getText();
            getClient().getExecutor().submit(new CommandSender(getGUIManager(), command));
            getGUIManager().getFieldOfCommands().setText(""); // set field text empty
        }
    }
}
