package Connections;


import GUI.Main;

import javax.swing.*;

public class ClientErrorHandler {

    public ClientErrorHandler(String messageError, JDialog dialog) {
        JOptionPane.showMessageDialog(Main.gui.getMainGUI(), messageError,
                "Exception with client", JOptionPane.ERROR_MESSAGE);
        SwingUtilities.invokeLater(dialog::dispose);
    }

}
