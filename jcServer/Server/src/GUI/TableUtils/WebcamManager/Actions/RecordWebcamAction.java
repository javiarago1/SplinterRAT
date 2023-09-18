package GUI.TableUtils.WebcamManager.Actions;

import GUI.TableUtils.WebcamManager.Events.RecordWebcamEvent;
import GUI.TableUtils.WebcamManager.Events.StopRecordingEvent;
import GUI.TableUtils.WebcamManager.WebcamGUI;
import Information.AbstractAction;

import javax.swing.*;
import java.awt.event.ActionEvent;

/*
 * Class for record or stop recording button actions
 */

public class RecordWebcamAction extends AbstractAction<WebcamGUI> {


    public RecordWebcamAction(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractButton abstractButton = (AbstractButton) e.getSource();
        boolean selected = abstractButton.getModel().isSelected();
        if (selected) {
            getGUIManager().getStartButton().setEnabled(false);
            getGUIManager().getRecordButton().setText("Stop recording");
            getGUIManager().getClient().getExecutor().submit(new RecordWebcamEvent(getGUIManager()));
        } else {
            getGUIManager().getSaveRecordButton().setEnabled(true);
            getGUIManager().getStartButton().setEnabled(true);
            getGUIManager().getRecordButton().setText("Record");
            getGUIManager().getClient().getExecutor().submit(new StopRecordingEvent(getGUIManager()));

        }
    }
}
