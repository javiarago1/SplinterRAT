package GUI.TableUtils.ScreenStreaming.Actions;

import GUI.TableUtils.ScreenStreaming.Events.ScreenShotEvent;
import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Information.AbstractAction;

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
