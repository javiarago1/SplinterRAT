package GUI.TableUtils.KeyboardController;

import Connections.Streams;
import GUI.SplinterGUI;
import GUI.TableUtils.Configuration.GetSYS;
import GUI.TableUtils.Configuration.SocketType;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class KeyboardControllerMenuListener implements ActionListener {

    private final SplinterGUI mainGUI;

    public KeyboardControllerMenuListener(SplinterGUI mainGUI) {
        this.mainGUI = mainGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Streams stream = Objects.requireNonNull(GetSYS.getClientHandler()).getMainStream();
        new KeyboardControllerGUI(mainGUI.getMainGUI(), stream);
    }
}
