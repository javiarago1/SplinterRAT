package TableUtils.WebcamManager.Events;

import TableUtils.WebcamManager.WebcamGUI;
import Utilities.AbstractEvent;

public abstract class WebcamEvent extends AbstractEvent<WebcamGUI> {
    public WebcamEvent(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }
}
