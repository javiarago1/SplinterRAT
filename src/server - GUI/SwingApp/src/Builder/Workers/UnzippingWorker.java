package Builder.Workers;

import javax.swing.*;

import Builder.Animations.UnzippingAnimationDialog;
import Builder.CompilerGUI;
import Compiler.ClientExtractor;

public class UnzippingWorker extends SwingWorker<Void, Void> {
    private final CompilerGUI compilerGUI;
    private final UnzippingAnimationDialog unzippingAnimationDialog;
    private boolean resultOfUnzipping;

    public UnzippingWorker(CompilerGUI compilerGUI) {
        this.compilerGUI = compilerGUI;
        unzippingAnimationDialog = new UnzippingAnimationDialog(compilerGUI);
    }

    @Override
    protected Void doInBackground() {
        ClientExtractor clientExtractor = new ClientExtractor();
        resultOfUnzipping = clientExtractor.call();
        return null;
    }

    @Override
    protected void done() {
        if (!resultOfUnzipping) {
            JOptionPane.showMessageDialog(compilerGUI, "JAR files might be corrupted or JAR " +
                            "doesn't have enough permissions to extract files!",
                    "Error extracting files", JOptionPane.ERROR_MESSAGE);
            compilerGUI.getCompileButton().setEnabled(false);

        } else {
            compilerGUI.getCompileButton().setEnabled(true);
        }
        unzippingAnimationDialog.closeDialog();

    }
}
