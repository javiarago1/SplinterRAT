package GUI.TableUtils.ScreenStreaming.Listeners;

import GUI.TableUtils.ScreenStreaming.Events.SendKeysEvent;
import GUI.TableUtils.ScreenStreaming.ScreenStreamerGUI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class MouseScreenListener extends MouseAdapter {

    private final ScreenStreamerGUI screenStreamerGUI;

    public MouseScreenListener(ScreenStreamerGUI screenStreamerGUI) {
        this.screenStreamerGUI = screenStreamerGUI;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        screenStreamerGUI.getQueueOfEvents().add(typeOfClick(e) + "," + x * 2 + "," + y * 2);
        screenStreamerGUI.getClient().getExecutor().submit(new SendKeysEvent(screenStreamerGUI));
    }

    private String typeOfClick(MouseEvent e) {
        return switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> "click/2,4";
            case MouseEvent.BUTTON3 -> "click/8,16";
            default -> "click/";
        };

    }
}
