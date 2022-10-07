package GUI.TableUtils.Configuration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class TableMenuListener extends MouseAdapter {

    private final JTable table;


    public TableMenuListener(JTable table) {
        this.table = table;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        Point point = e.getPoint();
        int currentRow = table.rowAtPoint(point);
        System.out.println(currentRow);
        table.setRowSelectionInterval(currentRow, currentRow);
    }





}
