package GUI.TableUtils.ScreenStreaming.Actions;

import GUI.TableUtils.ScreenStreaming.Events.StartStreamingEvent;
import GUI.TableUtils.ScreenStreaming.Events.StopStreamingEvent;
import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartStreamingAction implements ActionListener {

    private final ScreenStreamerGUI screenStreamerGUI;

    public StartStreamingAction(ScreenStreamerGUI screenStreamerGUI) {
        this.screenStreamerGUI = screenStreamerGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem stateButton = screenStreamerGUI.getStartMenu();
        if (stateButton.getText().equals("Start")) {
            stateButton.setText("Stop");
            screenStreamerGUI.getClient().getExecutor().submit(new StartStreamingEvent(screenStreamerGUI));

        } else {
            stateButton.setText("Start");
            screenStreamerGUI.getClient().getExecutor().submit(new StopStreamingEvent(screenStreamerGUI));
        }
    }
}
