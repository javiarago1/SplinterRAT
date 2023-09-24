package TableUtils.ScreenStreaming.Listeners;

import TableUtils.ScreenStreaming.ScreenStreamerGUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ScreenWindowAdapter extends WindowAdapter {

    private final ScreenStreamerGUI screenStreamerGUI;

    public ScreenWindowAdapter(ScreenStreamerGUI screenStreamerGUI) {

        this.screenStreamerGUI = screenStreamerGUI;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        JToggleButton toggleButton = screenStreamerGUI.getStartStopToggle();
        JCheckBox checkBox = screenStreamerGUI.getControlCheckBox();
        if (toggleButton.isSelected()) {
            screenStreamerGUI.getStartStopToggle().doClick();
        }
        if (checkBox.isSelected()) {
            screenStreamerGUI.getControlCheckBox().doClick();
        }
        e.getWindow().dispose();
    }
}
