package GUI.TableUtils.ScreenStreaming;

import Connections.Client;
import GUI.Main;
import GUI.TableUtils.ScreenStreaming.Actions.ScreenshotAction;
import GUI.TableUtils.ScreenStreaming.Actions.StartStreamingAction;
import GUI.TableUtils.ScreenStreaming.Actions.ControlComputerAction;
import GUI.TableUtils.ScreenStreaming.Events.EventListener;
import GUI.TableUtils.ScreenStreaming.Listeners.KeyScreenListener;
import GUI.TableUtils.ScreenStreaming.Listeners.MouseScreenListener;
import Information.GUIManagerInterface;


import javax.swing.*;
import java.awt.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ScreenStreamerGUI implements GUIManagerInterface {
    private final JDialog dialog;
    private final Client client;
    private final BlockingQueue<String> queueOfEvents = new LinkedBlockingQueue<>();
    private JMenuItem startMenu;
    private MouseScreenListener mouseScreenListener;
    private KeyScreenListener keyScreenListener;

    public ScreenStreamerGUI(Client client) {
        dialog = new JDialog(Main.gui.getMainGUI(), "Screen controller - " + client.getIdentifier());
        this.client = client;
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

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        dialog.add(streamingScreenShower, constraints);
        //dialog.addWindowListener(new StreamingWindowListener(isRunning));
        constraints.weighty = 0.0;

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        startMenu = new JMenuItem("Start");
        JCheckBoxMenuItem controlComputerMenu = new JCheckBoxMenuItem("Control computer");
        controlComputerMenu.addActionListener(new ControlComputerAction(this));
        JMenuItem screenshotMenu = new JMenuItem("Take screenshot");
        screenshotMenu.addActionListener(e -> new ScreenshotAction(this));
        menu.add(startMenu);
        menu.add(controlComputerMenu);
        menu.add(screenshotMenu);
        bar.add(menu);
        dialog.setJMenuBar(bar);
        startMenu.addActionListener(new StartStreamingAction(this));
    }

    public void removeControlComputerListeners() {
        streamingScreenShower.removeMouseListener(mouseScreenListener);
        streamingScreenShower.removeKeyListener(keyScreenListener);
        queueOfEvents.add("END");
    }

    public void addControlComputerListeners() {
        mouseScreenListener = new MouseScreenListener(this);
        streamingScreenShower.addMouseListener(mouseScreenListener);
        keyScreenListener = new KeyScreenListener(this);
        streamingScreenShower.addKeyListener(keyScreenListener);
        client.getExecutor().submit(new EventListener(this));
    }

    public JDialog getDialog() {
        return dialog;
    }

    public BlockingQueue<String> getQueueOfEvents() {
        return queueOfEvents;
    }

    public JLabel getStreamingScreenShower() {
        return streamingScreenShower;
    }

    public JMenuItem getStartMenu() {
        return startMenu;
    }

    public Client getClient() {
        return client;
    }
}
