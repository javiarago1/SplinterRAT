package GUI.TableUtils.WebcamManager.Actions;

import GUI.TableUtils.WebcamManager.Events.SaveRecordEvent;
import GUI.TableUtils.WebcamManager.WebcamGUI;

import java.awt.event.ActionEvent;


/*
 * Class for save button actions
 */
public class SaveRecordButton extends WebcamActions {


    public SaveRecordButton(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getWebcamGUI().getClient().getExecutor().submit(new SaveRecordEvent(getWebcamGUI()));
        getWebcamGUI().getSaveRecordButton().setEnabled(false);
    }
}
