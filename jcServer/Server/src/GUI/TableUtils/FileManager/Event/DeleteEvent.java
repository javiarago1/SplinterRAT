package GUI.TableUtils.FileManager.Event;

import Connections.Streams;
import Information.Action;

import java.util.List;

public class DeleteEvent extends Event {

    public DeleteEvent(Streams stream, List<String> CMElements) {
        super(stream, CMElements);
    }

    @Override
    public void run() {
        getStream().sendAndReadJSON(Action.DELETE, getCMElements());
    }
}
