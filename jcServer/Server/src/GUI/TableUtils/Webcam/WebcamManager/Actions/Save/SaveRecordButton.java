package GUI.TableUtils.Webcam.WebcamManager.Actions.Save;

import GUI.TableUtils.Webcam.WebcamManager.Actions.WebcamActions;
import GUI.TableUtils.Webcam.WebcamManager.WebcamGUI;

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
        if (!getWebcamGUI().isStateSaveRecordButton()) {
            getWebcamGUI().setStateSaveRecordButton(true);
            getWebcamGUI().getSaveRecordButton().setEnabled(false);
        }
    }
}
