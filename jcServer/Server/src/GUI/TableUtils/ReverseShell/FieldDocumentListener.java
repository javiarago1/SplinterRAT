package GUI.TableUtils.ReverseShell;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class FieldDocumentListener implements DocumentListener {


    private final ReverseShellGUI reverseShellGUI;

    public FieldDocumentListener(ReverseShellGUI reverseShellGUI) {

        this.reverseShellGUI = reverseShellGUI;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        fieldChanger(1);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // remove update is triggered when enter is pressed, so we check if enter was the key pressed before removing
        if (reverseShellGUI.isPressedEnter()) {
            reverseShellGUI.setPressedEnter(false);
        } else {
            fieldChanger(-1);
        }

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // No changed to be generated here
    }

    // update the state of text area when field text is being changed
    private void fieldChanger(int changer) {
        String textAreaContent = reverseShellGUI.getTextAreaOfResult().getText();
        String textOfCommandField = reverseShellGUI.getFieldOfCommands().getText();
        int distance = textAreaContent.length() - textOfCommandField.length() + changer;
        String textAreaContentWithOutCommand = textAreaContent.substring(0, distance);
        String finishResult = textAreaContentWithOutCommand + textOfCommandField;
        reverseShellGUI.getTextAreaOfResult().setText(finishResult);
    }


}
