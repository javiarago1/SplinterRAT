package GUI.TableUtils.Configuration;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StateColumnRenderer extends DefaultTableCellRenderer {
    public static final Color CONNECTED_COLOR = new Color(18, 169, 0);
    public static final Color DISCONNECTED_COLOR = new Color(192, 0, 15);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setHorizontalAlignment(JLabel.CENTER);

        String state = (String) value;

        if (state.equals("Connected")) {
            cell.setForeground(CONNECTED_COLOR);
        } else {
            cell.setForeground(DISCONNECTED_COLOR);
        }

        return cell;
    }


}
