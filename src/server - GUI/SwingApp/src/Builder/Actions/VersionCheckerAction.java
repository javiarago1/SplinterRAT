package Builder.Actions;

import Compiler.VersionChecker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VersionCheckerAction implements ActionListener {
    private final JTextField pathField;
    private final JDialog dialog;
    private final JButton compileButton;

    public VersionCheckerAction(JTextField pathField, JDialog dialog, JButton compileButton) {
        this.pathField = pathField;
        this.dialog = dialog;
        this.compileButton = compileButton;
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        int result = VersionChecker.checkVersion(pathField.getText());
        switch (result){
            case 0 -> compileButton.setEnabled(true);
            case -1 -> JOptionPane.showMessageDialog(dialog,
                    "To compile the client you need version 9 or higher.\n",
                    "Version not compatible", JOptionPane.WARNING_MESSAGE);
            case -2 ->  JOptionPane.showMessageDialog(dialog, "g++ wasn't found on this path",
                    "Not found error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
