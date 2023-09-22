package GUI.TableUtils.MessageBox.Actions;

import GUI.TableUtils.MessageBox.Events.MessageBoxEvent;
import GUI.TableUtils.MessageBox.MessageBoxGUI;
import Information.AbstractAction;
import org.json.JSONObject;

import java.awt.event.ActionEvent;

public class MessageBoxAction extends AbstractAction<MessageBoxGUI> {


    public MessageBoxAction(MessageBoxGUI guiManager) {
        super(guiManager);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", getGUIManager().getTitleTextField().getText());
        jsonObject.put("content", getGUIManager().getContentTextArea().getText());
        jsonObject.put("type", getGUIManager().getTypeOfBox().getSelectedIndex());
        jsonObject.put("icon", getGUIManager().getIconOfBox().getSelectedIndex());
        getClient().getExecutor().submit(new MessageBoxEvent(getGUIManager(), jsonObject));
    }
}
