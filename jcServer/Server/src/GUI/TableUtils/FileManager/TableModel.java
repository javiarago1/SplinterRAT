package GUI.TableUtils.FileManager;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {

    String[] col = {"Column 1", "Column 2", "Column 3"};

    public TableModel() {
        //Adding columns
        for (Object c : col)
            this.addColumn(c);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
