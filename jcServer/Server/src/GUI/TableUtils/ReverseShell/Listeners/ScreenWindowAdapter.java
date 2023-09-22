package GUI.TableUtils.ReverseShell.Listeners;

import GUI.TableUtils.ReverseShell.Events.CloseShellEvent;
import GUI.TableUtils.ReverseShell.ReverseShellGUI;
import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ScreenWindowAdapter extends WindowAdapter {

    private final ReverseShellGUI reverseShellGUI;

    public ScreenWindowAdapter(ReverseShellGUI reverseShellGUI) {

        this.reverseShellGUI = reverseShellGUI;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        reverseShellGUI.getClient().getExecutor().submit(new CloseShellEvent(reverseShellGUI));
    }
}

