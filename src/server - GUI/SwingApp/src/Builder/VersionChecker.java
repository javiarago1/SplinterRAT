package Builder;

import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VersionChecker implements ActionListener {
    private final JTextField pathField;
    private final JDialog dialog;
    private final JButton compileButton;

    public VersionChecker(JTextField pathField, JDialog dialog, JButton compileButton) {
        this.pathField = pathField;
        this.dialog = dialog;
        this.compileButton = compileButton;
    }

    static String[] getOSProperCommand() {
        String shell = "";
        String typeOfOpening = "";
        if (SystemUtils.IS_OS_WINDOWS) {
            shell = "cmd.exe";
            typeOfOpening = "/c";
        } else if (SystemUtils.IS_OS_LINUX) {
            shell = "/bin/bash";
            typeOfOpening = "-c";
        }
        return new String[]{shell, typeOfOpening};
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String[] commandArray = getOSProperCommand();
        ProcessBuilder processBuilder = new ProcessBuilder();
        String utilities = pathField.getText();
        String pathOfUtilities = "";
        if (!utilities.equals("g++ / windres")) pathOfUtilities = utilities + "\\";
        System.out.println(pathOfUtilities);
        processBuilder.command(commandArray[0], commandArray[1], pathOfUtilities + "g++ -dumpversion");
        try {

            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println(output);
                int finalPositionOfVersion = output.indexOf(".") == -1 ? output.length() : output.indexOf(".");
                if (Integer.parseInt(output.substring(0, finalPositionOfVersion).replaceAll("[\n\r]", "")) >= 9) {
                    compileButton.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(dialog,
                            "The version you have installed on your system is " + output +
                                    "To compile the client you need version 9 or higher.\n",
                            "Version not compatible", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "g++ wasn't found on this path",
                        "Not found error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
