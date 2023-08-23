package GUI.TableUtils.ScreenStreaming;

import Connections.ClientHandler;
import Connections.Streams;
import GUI.Main;
import GUI.TableUtils.Configuration.SocketType;


import javax.swing.*;
import java.awt.*;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ScreenStreamingGUI {
    private final JDialog dialog;
    private final Streams auxEventStream;
    private final ClientHandler clientHandler;
    private final Streams stream;
    private final AtomicBoolean isScreenshot = new AtomicBoolean(false);
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final ConcurrentLinkedQueue<String> queueOfEvents = new ConcurrentLinkedQueue<>();
    private JMenuItem startMenu;

    private final AtomicBoolean computerControl = new AtomicBoolean(false);

    public ScreenStreamingGUI(ClientHandler clientHandler) {
        this.stream = clientHandler.getStreamByName(SocketType.SCREEN);
        this.auxEventStream = clientHandler.getStreamByName(SocketType.SCREEN_EVENT);
        dialog = new JDialog(Main.gui.getMainGUI(), "Screen controller - " + clientHandler.getIdentifier());
        this.clientHandler = clientHandler;
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(600, 400);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        addComponents();
        dialog.setVisible(true);
    }



    private JLabel streamingScreenShower;

    public void addComponents() {
        GridBagConstraints constraints = new GridBagConstraints();
        streamingScreenShower = new JLabel("Press start to stream screen", SwingConstants.CENTER);
        streamingScreenShower.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        streamingScreenShower.setOpaque(true);
        streamingScreenShower.addMouseListener(new MouseScreenListener(queueOfEvents, computerControl));
        dialog.addKeyListener(new KeyScreenListener(queueOfEvents, computerControl));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        dialog.add(streamingScreenShower, constraints);
        dialog.addWindowListener(new StreamingWindowListener(isRunning));
        constraints.weighty = 0.0;

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        startMenu = new JMenuItem("Start");
        JCheckBoxMenuItem controlComputerMenu = new JCheckBoxMenuItem("Control computer");
        controlComputerMenu.addActionListener(e -> computerControl.set(((AbstractButton) e.getSource()).getModel().isSelected()));
        JMenuItem screenshotMenu = new JMenuItem("Take screenshot");
        screenshotMenu.addActionListener(e -> isScreenshot.set(true));
        menu.add(startMenu);
        menu.add(controlComputerMenu);
        menu.add(screenshotMenu);
        bar.add(menu);

        dialog.setJMenuBar(bar);
        startMenu.addActionListener(new StartAction(this));

    }

    public JDialog getDialog() {
        return dialog;
    }

    public Streams getStream() {
        return stream;
    }

    public AtomicBoolean getIsScreenshot() {
        return isScreenshot;
    }

    public ConcurrentLinkedQueue<String> getQueueOfEvents() {
        return queueOfEvents;
    }

    public JLabel getStreamingScreenShower() {
        return streamingScreenShower;
    }

    public AtomicBoolean getIsRunning() {
        return isRunning;
    }

    public JMenuItem getStartMenu() {
        return startMenu;
    }

    public AtomicBoolean getComputerControl() {
        return computerControl;
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public Streams getAuxEventStream() {
        return auxEventStream;
    }
}
