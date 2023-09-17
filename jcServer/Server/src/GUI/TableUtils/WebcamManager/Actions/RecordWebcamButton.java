package GUI.TableUtils.WebcamManager.Actions;

import GUI.TableUtils.WebcamManager.Events.RecordWebcamEvent;
import GUI.TableUtils.WebcamManager.Events.StopRecordingEvent;
import GUI.TableUtils.WebcamManager.WebcamGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/*
 * Class for record or stop recording button actions
 */

public class RecordWebcamButton extends WebcamActions {


    public RecordWebcamButton(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractButton abstractButton = (AbstractButton) e.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        if (selected) {
            getWebcamGUI().getStartButton().setEnabled(false);
            getWebcamGUI().getRecordButton().setText("Stop recording");
            getWebcamGUI().getClient().getExecutor().submit(new RecordWebcamEvent(getWebcamGUI()));
        } else {
            getWebcamGUI().getSaveRecordButton().setEnabled(true);
            getWebcamGUI().getStartButton().setEnabled(true);
            getWebcamGUI().getRecordButton().setText("Record");
            getWebcamGUI().getClient().getExecutor().submit(new StopRecordingEvent(getWebcamGUI()));

        }
    }
}
