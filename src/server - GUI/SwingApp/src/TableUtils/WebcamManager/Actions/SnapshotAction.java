package TableUtils.WebcamManager.Actions;

import TableUtils.WebcamManager.Events.SnapshotWebcamEvent;
import TableUtils.WebcamManager.WebcamGUI;
import Utilities.AbstractAction;

import java.awt.event.ActionEvent;

/*
 * Class for snapshot button actions
 */
public class SnapshotAction extends AbstractAction<WebcamGUI> {


    public SnapshotAction(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getClient().getExecutor().submit(new SnapshotWebcamEvent(getGUIManager()));
    }
}
