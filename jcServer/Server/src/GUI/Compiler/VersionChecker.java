package GUI.Compiler;

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

    @Override
    public void actionPerformed(ActionEvent e) {

        String path = pathField.getText();
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", path + " -dumpversion");
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
                if (Integer.parseInt(output.substring(0, output.indexOf("."))) >= 9) {
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
