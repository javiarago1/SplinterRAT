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
        JMenuItem stateButton = getGUIManager().getStartMenu();
        if (stateButton.getText().equals("Start")) {
            stateButton.setText("Stop");
            getClient().getExecutor().submit(new StartStreamingEvent(getGUIManager()));
            getGUIManager().getVirtualScreen().setText("");
        } else {
            stateButton.setText("Start");
            getClient().getExecutor().submit(new StopStreamingEvent(getGUIManager()));
            getGUIManager().getVirtualScreen().setText("Press start to stream screen");
        }
    }
}
