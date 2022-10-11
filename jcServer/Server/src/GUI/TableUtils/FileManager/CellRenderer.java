package GUI.TableUtils.FileManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Objects;

public class CellRenderer extends DefaultTableCellRenderer {

    private final FileManagerGUI fileManagerGUI;

    public CellRenderer(FileManagerGUI fileManagerGUI) {
        this.fileManagerGUI = fileManagerGUI;
    }

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus, int row,
                                                   int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


        if (row <= fileManagerGUI.getDivider())
            setIcon(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("Icons/folder.png"))));
        else setIcon(new ImageIcon(Objects.requireNonNull(this.getClass().getResource("Icons/file.png"))));

        return this;


    }

}