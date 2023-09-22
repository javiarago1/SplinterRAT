package GUI.TableUtils.KeyboardController.Actions;

import GUI.TableUtils.KeyboardController.Compiler.JCodeCompiler;
import GUI.TableUtils.KeyboardController.KeyboardControllerGUI;
import GUI.TableUtils.KeyboardController.Events.KeyboardEvent;
import Information.AbstractAction;


import java.awt.event.ActionEvent;

public class KeyboardAction extends AbstractAction<KeyboardControllerGUI> {
    public KeyboardAction(KeyboardControllerGUI keyboardControllerGUI) {
        super(keyboardControllerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String commandToSend = JCodeCompiler.compile(getGUIManager().getListModel());
        getClient().getExecutor().submit(new KeyboardEvent(getGUIManager(), commandToSend));
    }
}
