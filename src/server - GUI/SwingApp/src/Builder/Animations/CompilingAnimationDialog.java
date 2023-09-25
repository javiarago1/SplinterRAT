package Builder.Animations;


import ProgressBar.Animation;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class CompilingAnimationDialog extends JDialog {
    public CompilingAnimationDialog(JDialog parentDialog) {
        super(parentDialog, "Compiling client");
        this.setLocationRelativeTo(null);
        this.setSize(350, 180);
        this.setResizable(false);
        this.setLayout(new FlowLayout(FlowLayout.CENTER));

        JLabel coffeeLogo = new JLabel();
        ImageIcon coffeeIcon =
                new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("coffeeGIF.gif")));
        coffeeLogo.setIcon(coffeeIcon);
        this.add(coffeeLogo);
        JLabel information = new JLabel("It might take a while, grab your coffee and take a sip.");
        this.add(information);
        JLabel animationLabel = new JLabel();
        this.add(animationLabel);
        Timer timer = new Timer(0, new Animation("Compiling client", animationLabel));
        timer.setRepeats(true);
        timer.setDelay(500);
        timer.start();
        this.setVisible(true);
    }


}
