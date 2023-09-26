package ProgressBar;


import Utilities.AbstractDialogCreator;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public abstract class Bar<T extends AbstractDialogCreator> extends AbstractDialogCreator {
    private T fatherDialog;
    private final JLabel fileStateLabel;
    private final JProgressBar progressBar;
    private final JLabel actionAnimationLabel;
    // private final Server.Client client;
    private Timer timer;
    private final AtomicReference<String> fileState;

    private final JButton cancelOperation;

    private byte id;

    public Bar(T fatherDialog) {
        super(fatherDialog, fatherDialog.getClient(), "Progress");
        this.fileState = new AtomicReference<>("Zipping");
        this.setSize(400, 150);
        this.setResizable(false);
        this.setLayout(new GridBagLayout());
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
        this.add(progressBar, c);


        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        fileStateLabel = new JLabel("");
        this.add(fileStateLabel, c);


        actionAnimationLabel = new JLabel();
        actionAnimationLabel.setHorizontalAlignment(JLabel.RIGHT);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        this.add(actionAnimationLabel, c);



        this.setLocationRelativeTo(null);

        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        cancelOperation = new JButton("Cancel");
        this.add(cancelOperation, c);
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
        this.setVisible(true);
    }

    public JLabel getFileStateLabel() {
        return fileStateLabel;
    }

    public AtomicReference<String> getFileState() {
        return fileState;
    }

    @Override
    public void closeDialog() {
        timer.stop();
        this.dispose();
    }

    public JButton getCancelOperation() {
        return cancelOperation;
    }

}
