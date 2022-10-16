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
    private final JTextField compilerPathField;
    private final StringBuilder command = new StringBuilder();

    public Compiler(JDialog compilerDialog, JCheckBox[] checkBoxes, JTextField compilerPathField) {
        this.compilerDialog = compilerDialog;
        this.checkBoxes = checkBoxes;
        this.compilerPathField = compilerPathField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter(".exe", "."));
        chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        chooser.setSelectedFile(new File("client"));
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showSaveDialog(compilerDialog);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (checkBoxes[0].isSelected()) {
                command.append(compilerPathField.getText()).append(" client.cpp video_audio/DeviceEnumerator.cpp " +
                        "webcam/WebcamManager.cpp stream/Stream.cpp  " +
                        "time/Time.cpp  converter/Converter.cpp " +
                        "download/Download.cpp " +
                        "file/FileManager.cpp  " +
                        "information/system/SystemInformation.cpp  " +
                        "information/network/NetworkInformation.cpp " +
                        "-IC:opencv_static/include -Lopencv_static/lib " +
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
                        "-lzlib -lquirc -lwsock32 -lcomctl32 -lgdi32 " +
                        "-lole32 -lsetupapi -lws2_32  -loleaut32 -luuid" +
                        " -lcomdlg32 -lwininet -static-libgcc " +
                        "-static-libstdc++ -Wl,-Bstatic -lstdc++ -lpthread -Wl,-Bdynamic -o " +
                        chooser.getSelectedFile().getAbsolutePath());
            } else {

            }
            compile(command.toString());
        }
    }

    private void compile(String command) {
        new Thread(() -> {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("cmd.exe", "/c", command).directory(new File("../jcClient"));
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
                    System.out.println("No errors");
                } else {
                    System.out.println("Errors");
                }
                System.out.println(output);

            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

}
