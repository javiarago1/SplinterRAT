package Builder.Animations;

import Builder.CompilerGUI;

import javax.swing.*;
import java.awt.*;

public class UnzippingAnimationDialog extends JDialog {
    public UnzippingAnimationDialog(CompilerGUI frame) {
        super(frame, "Unzipping client files");
        setLayout(new BorderLayout(10, 10));
        setSize(300, 120);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Unzipping...");

        progressBar.setPreferredSize(new Dimension(progressBar.getPreferredSize().width, 10));

        progressBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(progressBar, BorderLayout.CENTER);
        adjustDialogPosition(frame, 20, 30);
    }

    public void showDialog(){
        setVisible(true);
    }

    private void adjustDialogPosition(Component parent, int xOffset, int yOffset) {
        Point parentLocation = parent.getLocation();
        setLocation(parentLocation.x + xOffset, parentLocation.y + yOffset);
    }
    public void closeDialog() {
        dispose();
    }
}
