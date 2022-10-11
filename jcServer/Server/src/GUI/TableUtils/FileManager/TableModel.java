package GUI.TableUtils.FileManager;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {

    String[] col = {"File name", "Size", "Creation date"};

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
