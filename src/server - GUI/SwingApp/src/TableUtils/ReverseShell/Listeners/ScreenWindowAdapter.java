package TableUtils.ReverseShell.Listeners;

import TableUtils.ReverseShell.Events.CloseShellEvent;
import TableUtils.ReverseShell.ReverseShellGUI;

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

