package GUI.TableUtils.KeyboardController;

import Connections.ClientErrorHandler;

import Information.Action;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class InstructionsSenderAction implements ActionListener {


    private final KeyboardControllerGUI keyboardControllerGUI;

    public InstructionsSenderAction(KeyboardControllerGUI keyboardControllerGUI) {

        this.keyboardControllerGUI = keyboardControllerGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String commandToSend = JCodeCompiler.compile(keyboardControllerGUI.getListModel());
        keyboardControllerGUI.getStream().getExecutor().submit(() -> {
            try {
                keyboardControllerGUI.getStream().sendAnd(Action.KEYBOARD_COMMAND, commandToSend);
            } catch (IOException ex) {
                new ClientErrorHandler("Unable to send keyboard command, connection lost with client",
                        keyboardControllerGUI.getDialog(),
                        keyboardControllerGUI.getStream().getClientSocket());
            }
        });
    }
}
