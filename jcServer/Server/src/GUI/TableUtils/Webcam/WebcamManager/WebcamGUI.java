package GUI.TableUtils.Webcam.WebcamManager;

import Connections.ClientHandler;
import Connections.Streams;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;
import GUI.TableUtils.Webcam.WebcamManager.MenuBar.FPSMenuListener;
import GUI.TableUtils.Webcam.WebcamManager.Actions.Record.RecordWebcamButton;
import GUI.TableUtils.Webcam.WebcamManager.Actions.Save.SaveRecordButton;
import GUI.TableUtils.Webcam.WebcamManager.Actions.Snapshot.SnapshotButton;
import GUI.TableUtils.Webcam.WebcamManager.Actions.Start.StartWebcamButton;
import GUI.TableUtils.Webcam.WebcamManager.Window.WebcamWindowListener;

import javax.swing.*;
import java.awt.*;


public class WebcamGUI {
    private final Streams stream;
    private final ClientHandler clientHandler;
    private int FPS = 30;
    private JComboBox<String> boxOfDevices;
    private JLabel webcamLabel;
    private JToggleButton startButton;
    private JToggleButton recordButton;
    private JButton snapshotButton;
    private JMenu recordingMenu;
    private JButton saveRecordButton;
    private JCheckBoxMenuItem videoFragmentedCheckBox;

    // Save current selected device in devices box
    private String selectedDevice;

    // Case where recording want to be saved and stop streaming
    private boolean saveAndStop;

    // State of streaming webcam
    private boolean stateStreamingButton;

    // State of button has been pressed to start recording (toggled)
    private boolean stateStartRecordButton;

    // State of button snapshot has been taken
    private boolean stateSnapshotButton;

    // State of stop button, has been pressed (toggled)
    private boolean stateStopRecordButton;

    // State of save button has been pressed
    private boolean stateSaveRecordButton;


    /*
     * Option to set de type of video saving.
     * - Fragmented: video will be saved in different fragments when stop recording button is pressed.
     * - Not fragmented: videos will be concatenated with each other while we press stop recording
     */
    private boolean fragmented;
    private JDialog webcamDialog;
    JMenuBar webcamMenuBar;
    JFrame mainGUI;


    public WebcamGUI(ClientHandler clientHandler, JFrame mainGUI) {
        this.clientHandler = clientHandler;
        this.stream = clientHandler.getStreamByName(SocketType.WEBCAM);
        stream.setWebcamDialogOpen(true);
        this.mainGUI = mainGUI;
        setUpDialog();

    }

    private void setUpDialog() {
        webcamDialog = new JDialog(mainGUI, "Webcam -" + clientHandler.getIdentifier());
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
        startButton.addActionListener(new StartWebcamButton(this));
        recordButton.addActionListener(new RecordWebcamButton(this));
        saveRecordButton.addActionListener(new SaveRecordButton(this));
        snapshotButton.addActionListener(new SnapshotButton(this));


        // adding menu bar
        addMenuBar();
        // getting the video devices working in client machine
        getDevices();

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

    private void getDevices() {
        stream.getExecutor().submit(new WebcamRequester(this));
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

    public boolean isStateStreamingButton() {
        return stateStreamingButton;
    }

    public void setStateStreamingButton(boolean stateStreamingButton) {
        this.stateStreamingButton = stateStreamingButton;
    }

    public boolean isStateStartRecordButton() {
        return stateStartRecordButton;
    }

    public JButton getSaveRecordButton() {
        return saveRecordButton;
    }

    public void setStateStartRecordButton(boolean stateStartRecordButton) {
        this.stateStartRecordButton = stateStartRecordButton;
    }

    public boolean isStateSnapshotButton() {
        return stateSnapshotButton;
    }

    public void setStateSnapshotButton(boolean stateSnapshotButton) {
        this.stateSnapshotButton = stateSnapshotButton;
    }

    public boolean isStateStopRecordButton() {
        return stateStopRecordButton;
    }

    public void setStateStopRecordButton(boolean stateStopRecordButton) {
        this.stateStopRecordButton = stateStopRecordButton;
    }

    public void setFrameDimensions(int width, int height) {
        webcamDialog.setSize(new Dimension(width, height));
    }


    public boolean isStateSaveRecordButton() {
        return stateSaveRecordButton;
    }

    public JCheckBoxMenuItem getVideoFragmentedCheckBox() {
        return videoFragmentedCheckBox;
    }

    public void setStateSaveRecordButton(boolean stateSaveRecordButton) {
        this.stateSaveRecordButton = stateSaveRecordButton;
    }

    public JMenu getRecordingMenu() {
        return recordingMenu;
    }

    public boolean isSaveAndStop() {
        return saveAndStop;
    }

    public void setSaveAndStop(boolean saveAndStop) {
        this.saveAndStop = saveAndStop;
    }


    public Streams getStream() {
        return stream;
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

    public void setFPS(int FPS) {
        this.FPS = FPS;
    }
}