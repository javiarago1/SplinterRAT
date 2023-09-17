package GUI.TableUtils.Webcam.WebcamManager.Events;

import Connections.Client;
import GUI.TableUtils.Webcam.WebcamManager.WebcamGUI;
import Information.AbstractEvent;

public abstract class WebcamEvent extends AbstractEvent<WebcamGUI> {
    public WebcamEvent(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }
}
