package TableUtils.ScreenStreaming.Listeners;

import TableUtils.ScreenStreaming.ScreenStreamerGUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseScreenListener extends MouseAdapter {

    private final ScreenStreamerGUI screenStreamerGUI;

    public MouseScreenListener(ScreenStreamerGUI screenStreamerGUI) {
        this.screenStreamerGUI = screenStreamerGUI;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        System.out.println("Valores de pantalla virtual " + x + " , " + y);
        Dimension originalDimensions = screenStreamerGUI.getOriginalScreenDimensions();
        Dimension virtualDimensions = screenStreamerGUI.getDimensionsOfVirtualScreen();
        double factorX = (double) originalDimensions.width / (double) virtualDimensions.width;
        double factorY = (double) originalDimensions.height / (double) virtualDimensions.height;
        int finalX = (int) Math.round(x * factorX);
        int finalY = (int) Math.round(y * factorY);
        System.out.println("Calculo final " + finalX + ", " + finalY);
        screenStreamerGUI.getQueueOfEvents().add(typeOfClick(e) + "," + finalX + "," + finalY);
    }

    private String typeOfClick(MouseEvent e) {
        return switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> "click/2,4";
            case MouseEvent.BUTTON3 -> "click/8,16";
            default -> "click/";
        };

    }
}
