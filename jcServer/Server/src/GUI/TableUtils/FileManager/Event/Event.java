package GUI.TableUtils.FileManager.Event;

import Connections.Client;

import java.util.List;

public abstract class Event implements Runnable {


    private Client client;
    private List<String> CMElements;

    public Event(Client client, List<String> CMElements) {

        this.client = client;
        this.CMElements = CMElements;
    }

    @Override
    public abstract void run();

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<String> getCMElements() {
        return CMElements;
    }

    public void setCMElements(List<String> CMElements) {
        this.CMElements = CMElements;
    }
}
