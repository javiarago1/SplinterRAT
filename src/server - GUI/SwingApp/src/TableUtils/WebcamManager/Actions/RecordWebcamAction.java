package TableUtils.WebcamManager.Actions;

import TableUtils.WebcamManager.Events.RecordWebcamEvent;
import TableUtils.WebcamManager.Events.StopRecordingEvent;
import TableUtils.WebcamManager.WebcamGUI;
import Utilities.Action.AbstractActionGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/*
 * Class for record or stop recording button actions
 */

public class RecordWebcamAction extends AbstractActionGUI<WebcamGUI> {

    public RecordWebcamAction(WebcamGUI guiManager) {
        super(guiManager);
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
