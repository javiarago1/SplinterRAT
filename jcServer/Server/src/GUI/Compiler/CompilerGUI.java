package GUI.Compiler;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;


public class CompilerGUI {

    private JDialog dialog;

    public CompilerGUI(Frame frame) {
        FlatDarculaLaf.setup();
        dialog = new JDialog(frame, "Compile Client");
        dialog.setLocationRelativeTo(null);
        dialog.setSize(400, 300);
        loadPanels();
        loadTabs();
        dialog.setVisible(true);
        encode();
    }

    private void encode() {
        File file = new File("C:\\Users\\JAVIER\\IdeaProjects\\jRat\\out\\artifacts\\Client_jar\\Client.jar");
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String encoded = Base64.getEncoder().encodeToString(fileContent);
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\JAVIER\\Documents\\testing\\encoded"));
            writer.write(encoded);

            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException("could not read file " + file, e);
        }
    }


    JPanel jPanelFirst;
    JPanel jPanelSecond;
    JPanel jPanelThird;

    private void loadPanels() {
        jPanelFirst = new javax.swing.JPanel();
        jPanelSecond = new javax.swing.JPanel();
        jPanelThird = new javax.swing.JPanel();
    }

    private JTabbedPane jTabbedPane;

    private void loadTabs() {
        jTabbedPane = new javax.swing.JTabbedPane();
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        jPanelFirst.setLayout(null);
        jTabbedPane.addTab("tab1", jPanelFirst);
        jPanelSecond.setLayout(null);
        jTabbedPane.addTab("tab2", jPanelSecond);
        jPanelThird.setLayout(null);
        jTabbedPane.addTab("tab3", jPanelThird);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(dialog.getContentPane());
        dialog.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTabbedPane)
        );


    }

}
