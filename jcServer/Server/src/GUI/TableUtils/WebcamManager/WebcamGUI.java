package GUI.TableUtils.WebcamManager;

import Connections.Client;
import GUI.TableUtils.WebcamManager.Actions.RecordWebcamAction;
import GUI.TableUtils.WebcamManager.Actions.SaveRecordAction;
import GUI.TableUtils.WebcamManager.Actions.SnapshotAction;
import GUI.TableUtils.WebcamManager.Actions.StartWebcamAction;
import GUI.TableUtils.WebcamManager.Listeners.FPSMenuListener;
import GUI.TableUtils.WebcamManager.Events.WebcamDevicesEvent;
import GUI.TableUtils.WebcamManager.Listeners.WebcamWindowListener;
import Information.GUIManagerInterface;

import javax.swing.*;
import java.awt.*;


public class WebcamGUI implements GUIManagerInterface {
    private final Client client;
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
    private JDialog webcamDialog;
    JMenuBar webcamMenuBar;
    JFrame mainGUI;


    public WebcamGUI(Client client, JFrame mainGUI) {
        this.client = client;
        this.mainGUI = mainGUI;
        client.setWebcamDialogOpen(true);
        setUpDialog();

    }

    private void setUpDialog() {
        webcamDialog = new JDialog(mainGUI, "Webcam -" + client.getIdentifier());
        webcamDialog.setSize(new Dimension(600, 400));
        webcamDialog.setMinimumSize(new Dimension(450, 300));
        webcamDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        webcamDialog.addWindowListener(new WebcamWindowListener(this));
        webcamDialog.setLocationRelativeTo(null);
        webcamDialog.getContentPane().setLayout(new GridBagLayout());

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
        webcamDialog.getContentPane().add(webcamLabel, constraints);
        constraints.weighty = 0.0;

        startButton = new JToggleButton("Start");
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        webcamDialog.getContentPane().add(startButton, constraints);

        recordButton = new JToggleButton("Record");
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        webcamDialog.getContentPane().add(recordButton, constraints);

        saveRecordButton = new JButton("Save records");
        constraints.gridx = 3;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        webcamDialog.getContentPane().add(saveRecordButton, constraints);

        snapshotButton = new JButton("Snapshot");
        constraints.gridx = 4;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        webcamDialog.getContentPane().add(snapshotButton, constraints);

        boxOfDevices = new JComboBox<>();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1.0;
        // component only resizes horizontally
        constraints.fill = GridBagConstraints.HORIZONTAL;
        webcamDialog.getContentPane().add(boxOfDevices, constraints);

        // adding listeners
        startButton.addActionListener(new StartWebcamAction(this));
        recordButton.addActionListener(new RecordWebcamAction(this));
        saveRecordButton.addActionListener(new SaveRecordAction(this));
        snapshotButton.addActionListener(new SnapshotAction(this));


        // adding menu bar
        addMenuBar();
        // getting the video devices working in client machine

        // Show webcam dialog
        webcamDialog.setVisible(true);
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
        webcamDialog.setJMenuBar(webcamMenuBar);
    }

    public void getDevices() {
        client.getExecutor().submit(new WebcamDevicesEvent(this));
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
        webcamDialog.setSize(new Dimension(width, height));
    }


    public JCheckBoxMenuItem getVideoFragmentedCheckBox() {
        return videoFragmentedCheckBox;
    }



    public JMenu getRecordingMenu() {
        return recordingMenu;
    }

    @Override
    public Client getClient() {
        return client;
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
        return webcamDialog;
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