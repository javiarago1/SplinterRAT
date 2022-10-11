package GUI.TableUtils.FileManager.Event;

import Connections.Streams;
import Information.Action;

import java.util.List;

public class PasteEvent extends Event {


    private final List<String> listWhereToPaste;

    public PasteEvent(Streams stream, List<String> CMElements, List<String> listWhereToPaste) {
        super(stream, CMElements);
        this.listWhereToPaste = listWhereToPaste;
    }

    @Override
    public void run() {
        getStream().sendAndReadJSON(Action.COPY, getCMElements(), listWhereToPaste);
    }
}
