package TableUtils.ScreenStreaming.Actions;

import TableUtils.ScreenStreaming.Events.ScreenShotEvent;
import TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Utilities.AbstractAction;

import java.awt.event.ActionEvent;

public class ScreenshotAction extends AbstractAction<ScreenStreamerGUI> {
    public ScreenshotAction(ScreenStreamerGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        getClient().getExecutor().submit(new ScreenShotEvent(getGUIManager()));
    }
}
