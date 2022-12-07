package GUI.TableUtils.MessageBox;

import Connections.ClientErrorHandler;
import Information.Action;

import java.io.IOException;

public class MessageBoxSender implements Runnable {

    private final MessageBoxGUI messageBoxGUI;
    private final String messageBoxInformation;

    public MessageBoxSender(MessageBoxGUI messageBoxGUI, String messageBoxInformation) {
        this.messageBoxGUI = messageBoxGUI;
        this.messageBoxInformation = messageBoxInformation;
    }

    @Override
    public void run() {
        try {
            messageBoxGUI.getStream().sendAction(Action.BOX_MESSAGE, messageBoxInformation);
            System.out.println(messageBoxInformation);
        } catch (IOException e) {
            new ClientErrorHandler("Unable to open message box, connection lost with client",
                    messageBoxGUI.getMessageBoxDialog(),
                    messageBoxGUI.getStream().getClientSocket());
        }
    }
}
