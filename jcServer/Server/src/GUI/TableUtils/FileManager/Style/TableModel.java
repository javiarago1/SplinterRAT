package GUI.TableUtils.FileManager.Style;

import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel {

    String[] col = {"File name", "Size"};

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
