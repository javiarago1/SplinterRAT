package TableUtils.ScreenStreaming.Listeners;

import TableUtils.ScreenStreaming.ScreenStreamerGUI;

import org.json.JSONArray;
import org.json.JSONObject;

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

        // Crear objeto JSON y añadirlo a la cola
        JSONObject clickEvent = new JSONObject();
        clickEvent.put("x", finalX);
        clickEvent.put("y", finalY);

        JSONObject clickType = typeOfClick(e);
        clickEvent.put("clickType", clickType.getString("type"));
        clickEvent.put("values", clickType.getJSONArray("values"));

        screenStreamerGUI.getQueueOfEvents().add(clickEvent.toString());
    }

    private JSONObject typeOfClick(MouseEvent e) {
        JSONObject clickType = new JSONObject();

        JSONArray valuesArray = new JSONArray();

        switch (e.getButton()) {
            case MouseEvent.BUTTON1 -> {
                clickType.put("type", "click");
                int[] values = new int[]{2, 4};
                for (int value : values) {
                    valuesArray.put(value);
                }
            }
            case MouseEvent.BUTTON3 -> {
                clickType.put("type", "click");
                int[] values = new int[]{8, 16};
                for (int value : values) {
                    valuesArray.put(value);
                }
            }
            default -> {
                clickType.put("type", "click");
            }
        }

        clickType.put("values", valuesArray);

        return clickType;
    }

}
