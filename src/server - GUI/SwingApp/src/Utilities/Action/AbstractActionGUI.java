package Utilities;

import java.awt.event.ActionEvent;

public abstract class AbstractActionGUI<T extends AbstractDialogCreator> extends AbstractGUIManager<T> implements AbstractAction {
    public AbstractActionGUI(T guiManager) {
        super(guiManager);
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);


}
