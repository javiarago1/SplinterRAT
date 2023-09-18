package GUI.TableUtils.WebcamManager.Actions;

import GUI.TableUtils.WebcamManager.Events.SaveRecordEvent;
import GUI.TableUtils.WebcamManager.WebcamGUI;
import Information.AbstractAction;

import java.awt.event.ActionEvent;

public class SaveRecordAction extends AbstractAction<WebcamGUI> {


    public SaveRecordAction(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getGUIManager().getClient().getExecutor().submit(new SaveRecordEvent(getGUIManager()));
        getGUIManager().getSaveRecordButton().setEnabled(false);
    }
}
