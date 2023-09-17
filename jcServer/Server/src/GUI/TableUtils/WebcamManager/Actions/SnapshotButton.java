package GUI.TableUtils.WebcamManager.Actions;

import GUI.TableUtils.WebcamManager.Events.SnapshotWebcamEvent;
import GUI.TableUtils.WebcamManager.WebcamGUI;

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
        getWebcamGUI().getClient().getExecutor().submit(new SnapshotWebcamEvent(getWebcamGUI()));
    }
}
