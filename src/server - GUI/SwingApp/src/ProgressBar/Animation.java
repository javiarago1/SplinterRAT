package ProgressBar;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Animation implements ActionListener {

    private int counter = 1;
    private AtomicReference<String> action;
    private final JLabel actionAnimationLabel;
    private String auxString;

    public Animation(AtomicReference<String> action, JLabel actionAnimationLabel) {
        this.action = action;
        this.actionAnimationLabel = actionAnimationLabel;
    }

    public Animation(String action, JLabel actionAnimationLabel) {
        this.auxString = action;
        this.actionAnimationLabel = actionAnimationLabel;
    }

    public String getAction() {
        return action == null ? auxString : action.get();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String dot = ".";
        String space = " ";
        actionAnimationLabel.setText(getAction() + dot.repeat(counter) + space.repeat(3 - counter));
        counter = counter == 3 ? 1 : counter + 1;
    }
}
