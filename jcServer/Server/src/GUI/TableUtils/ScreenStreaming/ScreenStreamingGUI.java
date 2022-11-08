package GUI.TableUtils.ScreenStreaming;

import Connections.Streams;
import GUI.Main;
import Information.Action;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class ScreenStreamingGUI {
    private final JDialog dialog;
    private final Streams stream;

    private final Queue<String> queueOfEvents = new LinkedList<>();

    public ScreenStreamingGUI(Streams stream) {
        this.stream = stream;
        dialog = new JDialog(Main.gui.getMainGUI());
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(null);
        addComponents();
        dialog.setVisible(true);
    }

    String[] dimensions;

    private int[] calculateRelativePosition(int x, int y) {
        return new int[]{x * Integer.parseInt(dimensions[0]) / streamingScreenShower.getWidth(), y * Integer.parseInt(dimensions[1]) / streamingScreenShower.getHeight()};
    }

    private JLabel streamingScreenShower;

    public void addComponents() {
        GridBagConstraints constraints = new GridBagConstraints();
        streamingScreenShower = new JLabel("Screen streaming");
        streamingScreenShower.setOpaque(true);
        streamingScreenShower.setBackground(Color.red);
        streamingScreenShower.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println(x + "|" + y);
                queueOfEvents.add("click/" + x * 2 + "," + y * 2);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weighty = 1.0;
        constraints.weightx = 1.0;
        dialog.add(streamingScreenShower, constraints);
        constraints.weighty = 0.0;

        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem startMenu = new JMenuItem("Start");
        menu.add(startMenu);
        bar.add(menu);

        dialog.setJMenuBar(bar);


        startMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {

                            stream.startScreen(Action.SCREEN_STREAM);
                            String received = stream.readString();
                            dimensions = received.split(",");
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        dialog.setSize(new Dimension(Integer.parseInt(dimensions[0]) / 2 + 15, Integer.parseInt(dimensions[1]) / 2 + 40));

                        while (true) {
                            byte[] array = null;
                            try {
                                if (queueOfEvents.isEmpty()) stream.sendString("null");
                                else {
                                    System.out.println(queueOfEvents.peek());
                                    stream.sendString(queueOfEvents.remove());

                                }
                                array = stream.receiveBytes();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            ImageIcon tempIMG = new ImageIcon(array);
                            Image img = tempIMG.getImage();
                            System.out.println(streamingScreenShower.getWidth() + "|" + streamingScreenShower.getHeight());
                            Image imgScale = img.getScaledInstance(streamingScreenShower.getWidth(), streamingScreenShower.getHeight(), Image.SCALE_SMOOTH);
                            SwingUtilities.invokeLater(() -> streamingScreenShower.setIcon(new ImageIcon(imgScale)));
                        }
                    }
                }).start();
            }
        });

    }

}
