package TableUtils.WebcamManager;


import Server.Client;
import TableUtils.WebcamManager.Actions.RecordWebcamAction;
import TableUtils.WebcamManager.Actions.SaveRecordAction;
import TableUtils.WebcamManager.Actions.SnapshotAction;

import TableUtils.WebcamManager.Actions.StartWebcamAction;
import TableUtils.WebcamManager.Events.WebcamDevicesEvent;
import TableUtils.WebcamManager.Listeners.FPSMenuListener;
import TableUtils.WebcamManager.Listeners.WebcamWindowListener;
import Utilities.AbstractDialogCreator;

import javax.swing.*;
import java.awt.*;
import Main.Main;


public class WebcamGUI extends AbstractDialogCreator {

    private int FPS = 30;
    private JComboBox<String> boxOfDevices;
    private JLabel webcamLabel;
    private JToggleButton startButton;
    private JToggleButton recordButton;
    private JButton snapshotButton;
    private JMenu recordingMenu;
    private JButton saveRecordButton;
    private JCheckBoxMenuItem videoFragmentedCheckBox;

    // Save current frame for snapshots

    private byte[] lastFrame;

    // Save current selected device in devices box
    private String selectedDevice;

    /*
     * Option to set de type of video saving.
     * - Fragmented: video will be saved in different fragments when stop recording button is pressed.
     * - Not fragmented: videos will be concatenated with each other while we press stop recording
     */
    private boolean fragmented;
    JMenuBar webcamMenuBar;



    public WebcamGUI(Client client, JFrame mainGUI) {
        super(Main.gui, client, "Webcam manager");

        client.setWebcamDialogOpen(true);
        setUpDialog();

    }

    private void setUpDialog() {
        this.setSize(new Dimension(600, 400));
        this.setMinimumSize(new Dimension(450, 300));
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WebcamWindowListener(this));
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(new GridBagLayout());

        // Setting up constraints
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(3, 3, 4, 3);

        // Setting up style and layout of components

        // Webcam label -> video streaming with image icon
        webcamLabel = new JLabel("Select and start a camera", SwingConstants.CENTER);
        webcamLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        webcamLabel.setOpaque(true);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 5;
        constraints.gridheight = 2;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        this.getContentPane().add(webcamLabel, constraints);
        constraints.weighty = 0.0;

        startButton = new JToggleButton("Start");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        this.getContentPane().add(startButton, constraints);

        recordButton = new JToggleButton("Record");
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        this.getContentPane().add(recordButton, constraints);

        saveRecordButton = new JButton("Save records");
        constraints.gridx = 3;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        this.getContentPane().add(saveRecordButton, constraints);

        snapshotButton = new JButton("Snapshot");
        constraints.gridx = 4;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        this.getContentPane().add(snapshotButton, constraints);

        boxOfDevices = new JComboBox<>();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        // component only resizes horizontally
        constraints.fill = GridBagConstraints.HORIZONTAL;
        this.getContentPane().add(boxOfDevices, constraints);

        // adding listeners
        startButton.addActionListener(new StartWebcamAction(this));
        recordButton.addActionListener(new RecordWebcamAction(this));
        saveRecordButton.addActionListener(new SaveRecordAction(this));
        snapshotButton.addActionListener(new SnapshotAction(this));


        // adding menu bar
        addMenuBar();
        // getting the video devices working in client machine

        // Show webcam dialog
        this.setVisible(true);
    }


    // method for setting up menu bar
    private void addMenuBar() {
        webcamMenuBar = new JMenuBar();
        recordingMenu = new JMenu("Recording settings");
        JMenuItem fpsLimit = new JMenuItem("Set FPS limit");
        videoFragmentedCheckBox = new JCheckBoxMenuItem("Record fragmented");
        recordingMenu.add(fpsLimit);
        recordingMenu.add(videoFragmentedCheckBox);
        webcamMenuBar.add(recordingMenu);
        fpsLimit.addActionListener(new FPSMenuListener(this));
        this.setJMenuBar(webcamMenuBar);
    }

    public void getDevices() {
        getClient().getExecutor().submit(new WebcamDevicesEvent(this));
    }


    public JToggleButton getStartButton() {
        return startButton;
    }


    public JToggleButton getRecordButton() {
        return recordButton;
    }


    public JButton getSnapshotButton() {
        return snapshotButton;
    }

    public JComboBox<String> getBoxOfDevices() {
        return boxOfDevices;
    }

    public JLabel getWebcamLabel() {
        return webcamLabel;
    }




    public JButton getSaveRecordButton() {
        return saveRecordButton;
    }


    public void setFrameDimensions(int width, int height) {
        this.setSize(new Dimension(width, height));
    }


    public JCheckBoxMenuItem getVideoFragmentedCheckBox() {
        return videoFragmentedCheckBox;
    }



    public JMenu getRecordingMenu() {
        return recordingMenu;
    }


    public String getSelectedDevice() {
        return selectedDevice;
    }

    public boolean isFragmented() {
        return fragmented;
    }

    public void setFragmented(boolean fragmented) {
        this.fragmented = fragmented;
    }

    public void setSelectedDevice(String selectedDevice) {
        this.selectedDevice = selectedDevice;
    }

    public JDialog getWebcamDialog() {
        return this;
    }

    public int getFPS() {
        return FPS;
    }

    public byte[] getLastFrame() {
        return lastFrame;
    }

    public void setLastFrame(byte[] lastFrame) {
        this.lastFrame = lastFrame;
    }

    public void setFPS(int FPS) {
        this.FPS = FPS;
    }
}