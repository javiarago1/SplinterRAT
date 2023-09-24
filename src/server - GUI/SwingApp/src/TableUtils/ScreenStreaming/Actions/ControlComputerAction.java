package TableUtils.ScreenStreaming.Actions;

import TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Utilities.AbstractAction;

import javax.swing.AbstractButton;
import java.awt.event.ActionEvent;

public class ControlComputerAction extends AbstractAction<ScreenStreamerGUI> {

    public ControlComputerAction(ScreenStreamerGUI screenStreamerGUI) {
        super(screenStreamerGUI);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof AbstractButton) {
            boolean isSelected = ((AbstractButton) source).getModel().isSelected();
            if (isSelected) {
                getGUIManager().addControlComputerListeners();
            } else {
                getGUIManager().removeControlComputerListeners();
            }
        }
    }
}

