package GUI.TableUtils.ScreenStreaming.Actions;

import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;
import Information.AbstractAction;
import Information.AbstractEvent;

import javax.swing.AbstractButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

