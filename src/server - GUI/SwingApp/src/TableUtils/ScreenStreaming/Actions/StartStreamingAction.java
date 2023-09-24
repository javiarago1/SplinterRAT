package TableUtils.ScreenStreaming.Actions;

import TableUtils.ScreenStreaming.Events.StartStreamingEvent;
import TableUtils.ScreenStreaming.Events.StopStreamingEvent;
import TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Utilities.AbstractAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class StartStreamingAction extends AbstractAction<ScreenStreamerGUI> {

    public StartStreamingAction(ScreenStreamerGUI screenStreamerGUI) {
        super(screenStreamerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton toggleButton = (JToggleButton) e.getSource();
        boolean isSelected = toggleButton.isSelected();
        if (isSelected) {
            toggleButton.setText("Stop");
            getClient().getExecutor().submit(new StartStreamingEvent(getGUIManager()));
            getGUIManager().getControlCheckBox().setEnabled(true);
            getGUIManager().getScreenshotButton().setEnabled(true);
            getGUIManager().getScreenSelector().setEnabled(false);
            getGUIManager().getVirtualScreen().setText("");
        } else {
            toggleButton.setText("Start");
            getGUIManager().getControlCheckBox().setEnabled(false);
            getGUIManager().getScreenshotButton().setEnabled(false);
            getGUIManager().getScreenSelector().setEnabled(true);
            getClient().getExecutor().submit(new StopStreamingEvent(getGUIManager()));
            getGUIManager().getVirtualScreen().setText("Press start to stream screen");
        }
    }
}
