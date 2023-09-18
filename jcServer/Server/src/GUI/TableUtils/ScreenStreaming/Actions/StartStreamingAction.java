package GUI.TableUtils.ScreenStreaming.Actions;

import GUI.TableUtils.ScreenStreaming.Events.StartStreamingEvent;
import GUI.TableUtils.ScreenStreaming.Events.StopStreamingEvent;
import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Information.AbstractAction;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
