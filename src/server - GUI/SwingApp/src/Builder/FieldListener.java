package Builder;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class FieldListener implements FocusListener {


    private final JTextField field;
    private final String defaultText;

    public FieldListener(JTextField field, String defaultText) {
        this.field = field;
        this.defaultText = defaultText;
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (field.getText().equals(defaultText)) {
            field.setText("");
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (field.getText().equals("")) {
            field.setText(defaultText);
        }
    }
}
