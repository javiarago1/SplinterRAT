package TableUtils.WebcamManager.Actions;

import TableUtils.WebcamManager.Events.SnapshotWebcamEvent;
import TableUtils.WebcamManager.WebcamGUI;
import Utilities.AbstractActionGUI;

import java.awt.event.ActionEvent;

/*
 * Class for snapshot button actions
 */
public class SnapshotAction extends AbstractActionGUI<WebcamGUI> {


    public SnapshotAction(WebcamGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getClient().getExecutor().submit(new SnapshotWebcamEvent(getGUIManager()));
    }
}
