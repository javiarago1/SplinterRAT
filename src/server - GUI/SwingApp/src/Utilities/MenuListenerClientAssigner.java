package Utilities;

import Server.Client;
import TableUtils.Credentials.CredentialsManagerGUI;
import Utilities.Action.AbstractAction;

import java.awt.event.ActionEvent;
import java.util.Optional;


public abstract class MenuListenerClientAssigner<T extends AbstractDialogCreator> implements AbstractAction {


    private final GUIFactory<T> guiFactory;

    public MenuListenerClientAssigner(GUIFactory<T> guiFactory) {
        this.guiFactory = guiFactory;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Optional.ofNullable(GetSYS.getClientHandler())
                .ifPresent(guiFactory::create);
    }

    public GUIFactory<T> getGuiFactory() {
        return guiFactory;
    }
}
