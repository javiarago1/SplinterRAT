package TableUtils.WebcamManager.Actions;

import TableUtils.WebcamManager.Events.SaveRecordEvent;
import TableUtils.WebcamManager.WebcamGUI;
import Utilities.AbstractActionGUI;

import java.awt.event.ActionEvent;

public class SaveRecordAction extends AbstractActionGUI<WebcamGUI> {
    public SaveRecordAction(WebcamGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getGUIManager().getClient().getExecutor().submit(new SaveRecordEvent(getGUIManager()));
        getGUIManager().getSaveRecordButton().setEnabled(false);
    }
}
