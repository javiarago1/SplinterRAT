package TableUtils.MessageBox.Actions;

import TableUtils.MessageBox.Events.MessageBoxEvent;
import TableUtils.MessageBox.MessageBoxGUI;
import Utilities.Action.AbstractActionGUI;
import org.json.JSONObject;

import java.awt.event.ActionEvent;

public class MessageBoxAction extends AbstractActionGUI<MessageBoxGUI> {


    public MessageBoxAction(MessageBoxGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ACTION", "SHOW_MESSAGE_BOX");
        JSONObject boxInformation = new JSONObject();
        boxInformation.put("title", getGUIManager().getTitleTextField().getText());
        boxInformation.put("content", getGUIManager().getContentTextArea().getText());
        boxInformation.put("type", getGUIManager().getTypeOfBox().getSelectedIndex());
        boxInformation.put("icon", getGUIManager().getIconOfBox().getSelectedIndex());
        jsonObject.put("info", boxInformation);
        getClient().getExecutor().submit(new MessageBoxEvent(getGUIManager(), jsonObject));
    }
}
