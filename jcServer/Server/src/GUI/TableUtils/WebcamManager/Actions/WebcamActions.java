package GUI.TableUtils.WebcamManager.Actions;


import GUI.TableUtils.WebcamManager.WebcamGUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Abstract class, constructor with Webcam object implementing Action Listener for buttons
 */

public abstract class WebcamActions implements ActionListener {

    private final WebcamGUI webcamGUI;

    public WebcamActions(WebcamGUI webcamGUI) {
        this.webcamGUI = webcamGUI;
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);

    public WebcamGUI getWebcamGUI() {
        return webcamGUI;
    }
}
