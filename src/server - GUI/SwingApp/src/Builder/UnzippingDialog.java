package Builder;

import javax.swing.*;
import java.awt.*;

public class UnzippingDialog extends JDialog {
    public UnzippingDialog(JFrame frame) {
        super(frame, "Unzipping client files");
        setLayout(new BorderLayout(10, 10));
        setSize(300, 120);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);


        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setString("Unzipping...");

        add(progressBar, BorderLayout.CENTER);

        setVisible(true);
    }

    public void closeDialog(){
        dispose();
    }

}
