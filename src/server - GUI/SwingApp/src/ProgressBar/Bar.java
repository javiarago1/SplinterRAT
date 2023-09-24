package ProgressBar;


import Utilities.AbstractDialogCreator;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Bar<T extends AbstractDialogCreator> {
    private T fatherDialog;
    private final JDialog dialog;
    private final JLabel fileStateLabel;
    private final JProgressBar progressBar;
    private final JLabel actionAnimationLabel;
    // private final Server.Client client;
    private Timer timer;
    private final AtomicReference<String> fileState;

    private final JButton cancelOperation;

    private byte id;

    public Bar(T fatherDialog) {
        this.fileState = new AtomicReference<>("Zipping");
        dialog = new JDialog(fatherDialog, "Progress");
        dialog.setSize(400, 150);
        dialog.setResizable(false);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        // Adding components & style

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        c.weightx = 1.0;
        c.insets = new Insets(4, 4, 4, 4);
        dialog.add(progressBar, c);


        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        fileStateLabel = new JLabel("");
        dialog.add(fileStateLabel, c);


        actionAnimationLabel = new JLabel();
        actionAnimationLabel.setHorizontalAlignment(JLabel.RIGHT);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        dialog.add(actionAnimationLabel, c);



        dialog.setLocationRelativeTo(null);

        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        cancelOperation = new JButton("Cancel");
        dialog.add(cancelOperation, c);
        startAnimation();
    }

    protected int totalRead;


    public abstract void updateProgress(int read, boolean isLastPacket);

    public void startAnimation() {
        timer = new Timer(0, new Animation(fileState, actionAnimationLabel));
        timer.setRepeats(true);
        timer.setDelay(500);
        timer.start();
    }

    public byte getId() {
        return id;
    }

    public void setProgressBarVisible() {
        dialog.setVisible(true);
    }

    public JDialog getDialog() {
        return dialog;
    }


    public JLabel getFileStateLabel() {
        return fileStateLabel;
    }

    public AtomicReference<String> getFileState() {
        return fileState;
    }



    public void close() {
        timer.stop();
        dialog.dispose();
    }

    public JButton getCancelOperation() {
        return cancelOperation;
    }

    public void setId(byte id) {
        this.id = id;
    }
}
