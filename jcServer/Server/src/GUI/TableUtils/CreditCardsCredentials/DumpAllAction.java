package GUI.TableUtils.CreditCardsCredentials;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DumpAllAction implements ActionListener {
    private final CredentialsManagerGUI credentialsManagerGUI;

    public DumpAllAction(CredentialsManagerGUI credentialsManagerGUI) {
        this.credentialsManagerGUI = credentialsManagerGUI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        credentialsManagerGUI.getDumpAllJButton().setEnabled(false);
        credentialsManagerGUI.getStream().getExecutor().submit(new DumpAllSender(credentialsManagerGUI));

    }
}
