package GUI.TableUtils.Webcam.WebcamManager.MenuBar;

import GUI.TableUtils.Webcam.WebcamManager.Actions.WebcamActions;
import GUI.TableUtils.Webcam.WebcamManager.WebcamGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FPSMenuListener extends WebcamActions {


    public FPSMenuListener(WebcamGUI webcamGUI) {
        super(webcamGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int min = 0, max = 120;
        String input;
        int transformedResult = 0;
        boolean validNumber = false;
        do {
            input = JOptionPane.showInputDialog(getWebcamGUI().getWebcamDialog(),
                    "Set the fps to record. It is recommended not to exceed 30 FPS (Not recommended to be changed):",
                    getWebcamGUI().getFPS());
            if (input == null || input.isEmpty()) {
                showErrorWindow();
            } else {
                try {
                    transformedResult = Integer.parseInt(input);
                    if (input.matches("\\d+") && transformedResult > min && transformedResult <= max) {
                        getWebcamGUI().setFPS(transformedResult);
                        validNumber = true;
                    } else {
                        validNumber = false;
                        showErrorWindow();
                    }
                } catch (NumberFormatException ex) {
                    showErrorWindow();
                }
            }
        } while (input == null || !input.matches("\\d+") || !validNumber || transformedResult <= min || transformedResult > max);
    }

    private void showErrorWindow() {
        JOptionPane.showMessageDialog(getWebcamGUI().getWebcamDialog(), "Enter proper FPS values (0-120 FPS). Only numbers.",
                "Inadequate FPS", JOptionPane.ERROR_MESSAGE);
    }
}
