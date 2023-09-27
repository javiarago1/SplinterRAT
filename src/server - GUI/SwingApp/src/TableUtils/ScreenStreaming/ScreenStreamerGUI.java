package TableUtils.ScreenStreaming;

import Server.Client;
import TableUtils.ScreenStreaming.Actions.ScreenshotAction;
import TableUtils.ScreenStreaming.Actions.StartStreamingAction;
import TableUtils.ScreenStreaming.Actions.ControlComputerAction;
import TableUtils.ScreenStreaming.Events.EventListener;
import TableUtils.ScreenStreaming.Events.MonitorsEvent;
import TableUtils.ScreenStreaming.Listeners.KeyScreenListener;
import TableUtils.ScreenStreaming.Listeners.MouseScreenListener;
import TableUtils.ScreenStreaming.Listeners.ScreenWindowAdapter;
import Utilities.AbstractDialogCreator;


import javax.swing.*;
import java.awt.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import Main.Main;
import Utilities.AbstractDialogCreatorWUpdater;

public class ScreenStreamerGUI extends AbstractDialogCreatorWUpdater {
    private final BlockingQueue<String> queueOfEvents = new LinkedBlockingQueue<>();
    private MouseScreenListener mouseScreenListener;
    private KeyScreenListener keyScreenListener;

    private Dimension originalScreenDimensions;

    private JComboBox<String> screenSelector;

    private JToggleButton startStopToggle;

    private JCheckBox controlCheckBox;

    private JButton screenshotButton;

    private byte[] lastData;

    public ScreenStreamerGUI(Client client) {
        super(Main.gui, client, "Screen controller");
        this.setLayout(new BorderLayout());
        this.setSize(1000, 600);
        this.setLocationRelativeTo(null);
        addComponents();
        requestMonitors();
        this.setVisible(true);
    }

    private JLabel virtualScreen;


    public void addComponents() {
        virtualScreen = new JLabel("Press start to stream screen", SwingConstants.CENTER);
        virtualScreen.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        virtualScreen.setOpaque(true);
        this.add(virtualScreen, BorderLayout.CENTER);
        virtualScreen.setFocusable(true);

        JPanel toolbar = new JPanel();
        toolbar.setBackground(new Color(47, 50, 51));
        toolbar.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        startStopToggle = new JToggleButton("Start");
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        toolbar.add(startStopToggle, gbc);
        startStopToggle.setFocusable(false);

        screenSelector = new JComboBox<>();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        toolbar.add(screenSelector, gbc);
        screenSelector.setFocusable(false);

        screenshotButton = new JButton("Screenshot");
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        toolbar.add(screenshotButton, gbc);
        screenshotButton.setEnabled(false);
        screenshotButton.addActionListener(new ScreenshotAction(this));
        screenshotButton.setFocusable(false);

        controlCheckBox = new JCheckBox("Control");
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        toolbar.add(controlCheckBox, gbc);
        controlCheckBox.setEnabled(false);
        controlCheckBox.addActionListener(new ControlComputerAction(this));
        controlCheckBox.setFocusable(false);

        this.add(toolbar, BorderLayout.SOUTH);
        startStopToggle.addActionListener(new StartStreamingAction(this));
        this.addWindowListener(new ScreenWindowAdapter(this));
    }

    public void requestMonitors() {
        getClient().getExecutor().submit(new MonitorsEvent(this));
    }

    public void removeControlComputerListeners() {
        virtualScreen.removeMouseListener(mouseScreenListener);
        virtualScreen.removeKeyListener(keyScreenListener);
        queueOfEvents.add("END");
    }

    public void addControlComputerListeners() {
        mouseScreenListener = new MouseScreenListener(this);
        virtualScreen.addMouseListener(mouseScreenListener);
        keyScreenListener = new KeyScreenListener(this);
        virtualScreen.addKeyListener(keyScreenListener);
        getClient().getExecutor().submit(new EventListener(this));
    }

    public BlockingQueue<String> getQueueOfEvents() {
        return queueOfEvents;
    }

    public JLabel getVirtualScreen() {
        return virtualScreen;
    }

    public Dimension getDimensionsOfVirtualScreen() {
        return new Dimension(virtualScreen.getWidth(), virtualScreen.getHeight());
    }

    public JComboBox<String> getScreenSelector() {
        return screenSelector;
    }

    public Dimension getOriginalScreenDimensions() {
        return originalScreenDimensions;
    }


    public JCheckBox getControlCheckBox() {
        return controlCheckBox;
    }

    public JButton getScreenshotButton() {
        return screenshotButton;
    }

    public void setOriginalScreenDimensions(int a, int b) {
        this.originalScreenDimensions = new Dimension(a, b);
    }

    public JToggleButton getStartStopToggle() {
        return startStopToggle;
    }

    public byte[] getLastData(){
        return lastData;
    }

    public void setLastData(byte []data){
        lastData = data;
    }

    @Override
    public void addToSwingUpdater() {
        getSwingUpdater().setScreenStreamerGUI(this);
    }
}
