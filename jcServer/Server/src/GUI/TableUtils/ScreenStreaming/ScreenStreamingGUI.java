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

public class ScreenStreamingGUI {
    private final JDialog dialog;
    private final Streams stream;

    public ScreenStreamingGUI(Streams stream) {
        this.stream = stream;
        dialog = new JDialog();
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(null);
        addComponents();
        dialog.setVisible(true);
    }

    public void addComponents() {
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel streamingScreenShower = new JLabel("Screen streaming");
        streamingScreenShower.setOpaque(true);
        streamingScreenShower.setBackground(Color.red);
        streamingScreenShower.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println(x + "," + y);
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
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        while (true) {
                            byte[] array = null;
                            try {
                                stream.sendString("200,200");
                                array = stream.receiveBytes();

                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                            ImageIcon tempIMG = new ImageIcon(array);
                            Image img = tempIMG.getImage();
                            Image imgScale = img.getScaledInstance(streamingScreenShower.getWidth(), streamingScreenShower.getHeight(), Image.SCALE_SMOOTH);
                            SwingUtilities.invokeLater(() -> streamingScreenShower.setIcon(new ImageIcon(imgScale)));
                            System.out.println(streamingScreenShower.getWidth() + "|" + streamingScreenShower.getHeight());

                        }
                    }
                }).start();
            }
        });

    }

}
