package GUI.TableUtils.FileManager.Event;

import Connections.Streams;

import java.util.List;

public abstract class Event implements Runnable {


    private Streams stream;
    private List<String> CMElements;

    public Event(Streams stream, List<String> CMElements) {

        this.stream = stream;
        this.CMElements = CMElements;
    }

    @Override
    public abstract void run();

    public Streams getStream() {
        return stream;
    }

    public void setStream(Streams stream) {
        this.stream = stream;
    }

    public List<String> getCMElements() {
        return CMElements;
    }

    public void setCMElements(List<String> CMElements) {
        this.CMElements = CMElements;
    }
}
