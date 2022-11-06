package GUI.TableUtils.MessageBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageBoxAction implements ActionListener {

    private final MessageBoxGUI messageBoxGUI;

    public MessageBoxAction(MessageBoxGUI messageBoxGUI) {

        this.messageBoxGUI = messageBoxGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String boxInformation = messageBoxGUI.getTitleTextField().getText() + "|" +
                messageBoxGUI.getContentTextArea().getText() + "|" +
                messageBoxGUI.getTypeOfBox().getSelectedIndex() + "|" +
                messageBoxGUI.getIconOfBox().getSelectedIndex();

        messageBoxGUI.getStream().getExecutor().submit(new MessageBoxSender(messageBoxGUI, boxInformation));
    }
}
