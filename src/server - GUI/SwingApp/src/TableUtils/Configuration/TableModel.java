package TableUtils.Configuration;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {

    public TableModel(Object[] columnNames) {
        super(null, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 2;
    }


}

