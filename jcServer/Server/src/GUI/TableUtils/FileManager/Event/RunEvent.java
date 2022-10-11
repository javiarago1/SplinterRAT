package GUI.TableUtils.FileManager.Event;

import Connections.Streams;
import Information.Action;

import java.util.List;

public class RunEvent extends Event {

    public RunEvent(Streams stream, List<String> CMElements) {
        super(stream, CMElements);
    }

    @Override
    public void run() {
        getStream().sendAndReadJSON(Action.RUN, getCMElements());
    }
}
