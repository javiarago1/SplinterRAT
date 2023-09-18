package GUI.TableUtils.WebcamManager.Actions;

import GUI.TableUtils.WebcamManager.Events.SnapshotWebcamEvent;
import GUI.TableUtils.WebcamManager.WebcamGUI;
import Information.AbstractAction;

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
