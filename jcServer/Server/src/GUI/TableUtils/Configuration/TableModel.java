package GUI.TableUtils.Configuration;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {

    public TableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 2;
    }



}

