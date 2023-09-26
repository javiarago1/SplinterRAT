package TableUtils.KeyboardController.Actions;

import TableUtils.KeyboardController.Compiler.JCodeCompiler;
import TableUtils.KeyboardController.KeyboardControllerGUI;
import TableUtils.KeyboardController.Events.KeyboardEvent;
import Utilities.AbstractActionGUI;


import java.awt.event.ActionEvent;

public class KeyboardAction extends AbstractActionGUI<KeyboardControllerGUI> {
    public KeyboardAction(KeyboardControllerGUI keyboardControllerGUI) {
        super(keyboardControllerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String commandToSend = JCodeCompiler.compile(getGUIManager().getListModel());
        getClient().getExecutor().submit(new KeyboardEvent(getGUIManager(), commandToSend));
    }
}
