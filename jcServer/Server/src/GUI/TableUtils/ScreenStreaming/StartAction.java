package GUI.TableUtils.ScreenStreaming;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartAction implements ActionListener {

    private final ScreenStreamingGUI screenStreamingGUI;

    public StartAction(ScreenStreamingGUI screenStreamingGUI) {
        this.screenStreamingGUI = screenStreamingGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem stateButton = screenStreamingGUI.getStartMenu();
        if (stateButton.getText().equals("Start")) {
            screenStreamingGUI.getIsRunning().set(true);
            screenStreamingGUI.getStream().getExecutor().submit(new Streamer(screenStreamingGUI));
            stateButton.setText("Stop");
        } else {
            screenStreamingGUI.getIsRunning().set(false);
            stateButton.setText("Start");
        }
    }
}
