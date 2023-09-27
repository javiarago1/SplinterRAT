package Utilities;

import Server.Client;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class AbstractMWDialogCreator extends AbstractDialogCreatorWUpdater implements MultipleWindow {

    private byte windowId;
    public AbstractMWDialogCreator(Window window, Client client, String title) {
        super(window, client, title);
        deleteWindowHandler();
    }


    public void deleteWindowHandler(){
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                getSwingUpdater().closeFileManagerWindowHandler(windowId);
            }
        });
    }
    @Override
    public byte getWindowId(){
        return windowId;
    }

    @Override
    public void setWindowId(byte id) {
        windowId = id;
    }
}
