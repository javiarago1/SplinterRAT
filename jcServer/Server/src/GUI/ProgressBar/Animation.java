package GUI.ProgressBar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Animation implements ActionListener {

    private int counter = 1;
    private final String action;

    private final JLabel actionAnimationLabel;

    public Animation(String action, JLabel actionAnimationLabel) {
        this.action = action;
        this.actionAnimationLabel = actionAnimationLabel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String dot = ".";
        String space = " ";
        actionAnimationLabel.setText(action + dot.repeat(counter) + space.repeat(3 - counter));
        counter = counter == 3 ? 1 : counter + 1;
    }
}
