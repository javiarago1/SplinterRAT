package GUI.TableUtils.ScreenStreaming;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class MouseScreenListener extends MouseAdapter {

    private final ConcurrentLinkedQueue<String> queueOfEvents;
    private final AtomicBoolean computerControl;

    public MouseScreenListener(ConcurrentLinkedQueue<String> queueOfEvents, AtomicBoolean computerControl) {
        this.queueOfEvents = queueOfEvents;
        this.computerControl = computerControl;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (computerControl.get()) {
            int x = e.getX();
            int y = e.getY();
            queueOfEvents.add(typeOfClick(e) + "," + x * 2 + "," + y * 2);
        }
    }

    private String typeOfClick(MouseEvent e) {
        return switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> "click/2,4";
            case MouseEvent.BUTTON3 -> "click/8,16";
            default -> "click/";
        };

    }
}
