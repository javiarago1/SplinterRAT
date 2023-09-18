package Information;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AbstractAction<T extends GUIManagerInterface> extends AbstractGUIManager<T> implements ActionListener {
    public AbstractAction(T guiManager) {
        super(guiManager);
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);
}
