package GUI.ProgressBar;


import Connections.Streams;
import Information.Time;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class Bar extends SwingWorker<Void, Object> {
    private final JDialog dialog;
    private final JLabel fileStateLabel;
    private final JProgressBar progressBar;
    private final JLabel actionAnimationLabel;
    private Timer timer;
    private final String action;

    public Bar(JDialog parentDialog, String action) {
        this.action = action;
        dialog = new JDialog(parentDialog, action + " progress bar");
        dialog.setSize(400, 100);
        dialog.setResizable(false);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        // Adding components & style

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(6, 4, 2, 4);
        dialog.add(progressBar, c);
        c.insets = new Insets(2, 6, 2, 6);


        fileStateLabel = new JLabel("File");
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        dialog.add(fileStateLabel, c);


        actionAnimationLabel = new JLabel();
        actionAnimationLabel.setHorizontalAlignment(JLabel.RIGHT);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        dialog.add(actionAnimationLabel, c);


        startAnimation();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }


    abstract protected Void doInBackground();

    protected void process(List<Object> chunks) {
        getProgressBar().setValue((Integer) chunks.get(chunks.size() - 2));   // case of integer for %
        getFileStateLabel().setText(String.valueOf(chunks.get(chunks.size() - 1)));  // case of string for current file
    }

    protected void done() {
        close();
    }

    public void startAnimation() {
        timer = new Timer(0, new Animation(action, actionAnimationLabel));
        timer.setRepeats(true);
        timer.setDelay(500);
        timer.start();
    }

    public JLabel getFileStateLabel() {
        return fileStateLabel;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JDialog getDialog() {
        return dialog;
    }


    public void close() {
        timer.stop(); // stop animation
        dialog.dispose();   // close dialog
    }

}
