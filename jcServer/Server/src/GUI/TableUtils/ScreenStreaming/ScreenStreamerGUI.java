package GUI.TableUtils.ScreenStreaming;

import Connections.Client;
import GUI.Main;
import GUI.TableUtils.ScreenStreaming.Actions.ScreenshotAction;
import GUI.TableUtils.ScreenStreaming.Actions.StartStreamingAction;
import GUI.TableUtils.ScreenStreaming.Actions.ControlComputerAction;
import GUI.TableUtils.ScreenStreaming.Events.EventListener;
import GUI.TableUtils.ScreenStreaming.Events.MonitorsEvent;
import GUI.TableUtils.ScreenStreaming.Listeners.KeyScreenListener;
import GUI.TableUtils.ScreenStreaming.Listeners.MouseScreenListener;
import GUI.TableUtils.ScreenStreaming.Listeners.ScreenWindowAdapter;
import Information.GUIManagerInterface;


import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ScreenStreamerGUI implements GUIManagerInterface {
    private final JDialog dialog;
    private final Client client;
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
        dialog = new JDialog(Main.gui.getMainGUI(), "Screen controller - " + client.getIdentifier());
        this.client = client;
        dialog.setLayout(new BorderLayout());
        dialog.setSize(1000, 600);
        dialog.setLocationRelativeTo(null);
        addComponents();
        dialog.setVisible(true);
    }

    private JLabel virtualScreen;


    public void addComponents() {
        virtualScreen = new JLabel("Press start to stream screen", SwingConstants.CENTER);
        virtualScreen.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        virtualScreen.setOpaque(true);
        dialog.add(virtualScreen, BorderLayout.CENTER);

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

        screenSelector = new JComboBox<>();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        toolbar.add(screenSelector, gbc);

        screenshotButton = new JButton("Screenshot");
        gbc.gridx = 2;
        gbc.weightx = 1.0;
        toolbar.add(screenshotButton, gbc);
        screenshotButton.setEnabled(false);
        screenshotButton.addActionListener(new ScreenshotAction(this));

        controlCheckBox = new JCheckBox("Control");
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        toolbar.add(controlCheckBox, gbc);
        controlCheckBox.setEnabled(false);

        dialog.add(toolbar, BorderLayout.SOUTH);
        startStopToggle.addActionListener(new StartStreamingAction(this));
        requestMonitors();

        dialog.addWindowListener(new ScreenWindowAdapter(this));
    }

    private void requestMonitors() {
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
        client.getExecutor().submit(new EventListener(this));
    }


    public JDialog getDialog() {
        return dialog;
    }

    public BlockingQueue<String> getQueueOfEvents() {
        return queueOfEvents;
    }

    public JLabel getVirtualScreen() {
        return virtualScreen;
    }


    public Client getClient() {
        return client;
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

}
