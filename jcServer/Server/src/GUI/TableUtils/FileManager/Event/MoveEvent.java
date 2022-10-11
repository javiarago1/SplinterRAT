package GUI.TableUtils.FileManager.Event;

import Connections.Streams;
import Information.Action;

import java.util.List;

public class MoveEvent extends Event {
    private final String directoryToMove;

    public MoveEvent(Streams stream, List<String> CMElements, String directoryToMove) {
        super(stream, CMElements);
        this.directoryToMove = directoryToMove;
    }

    @Override
    public void run() {
        getStream().sendAndReadJSON(Action.MOVE, getCMElements(), directoryToMove);
    }
}
