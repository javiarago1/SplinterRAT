package GUI.TableUtils.WebcamManager.Events;

import GUI.TableUtils.WebcamManager.WebcamGUI;
import Information.AbstractEvent;

public abstract class WebcamEvent extends AbstractEvent<WebcamGUI> {
    public WebcamEvent(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }
}
