package GUI.TableUtils.Webcam.WebcamManager.Actions.Snapshot;

import GUI.TableUtils.Webcam.WebcamManager.Actions.WebcamActions;
import GUI.TableUtils.Webcam.WebcamManager.WebcamGUI;

import java.awt.event.ActionEvent;

/*
 * Class for snapshot button actions
 */
public class SnapshotButton extends WebcamActions {


    public SnapshotButton(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getWebcamGUI().setStateSnapshotButton(true);
    }
}
