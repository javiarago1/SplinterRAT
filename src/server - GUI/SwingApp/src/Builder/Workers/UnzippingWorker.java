package Builder.Workers;

import javax.swing.*;

import Builder.Animations.UnzippingAnimationDialog;
import Builder.CompilerGUI;
import Compiler.ClientExtractor;

public class UnzippingWorker extends SwingWorker<Void, Void> {
    private final CompilerGUI compilerGUI;
    private final UnzippingAnimationDialog unzippingAnimationDialog;
    private final boolean overwriteExisting;
    private boolean resultOfUnzipping;

    public UnzippingWorker(CompilerGUI compilerGUI, boolean overwriteExisting) {
        this.compilerGUI = compilerGUI;
        this.overwriteExisting = overwriteExisting;
        unzippingAnimationDialog = new UnzippingAnimationDialog(compilerGUI);
        compilerGUI.getCheckButton().setEnabled(false);
        compilerGUI.getCompileButton().setEnabled(false);
        compilerGUI.getExtractButton().setEnabled(false);
    }

    @Override
    protected Void doInBackground() {
        ClientExtractor clientExtractor = new ClientExtractor();
        if (!overwriteExisting && clientExtractor.checkIfClientFilesAlreadyExists()){
            resultOfUnzipping =true;
        } else {
            SwingUtilities.invokeLater(unzippingAnimationDialog::showDialog);
            resultOfUnzipping = clientExtractor.call();
        }
        return null;
    }

    @Override
    protected void done() {
        unzippingAnimationDialog.closeDialog();
        if (resultOfUnzipping) {
            compilerGUI.getCheckButton().setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(compilerGUI, "JAR files might be corrupted or JAR " +
                            "doesn't have enough permissions to extract files!",
                    "Error extracting files", JOptionPane.ERROR_MESSAGE);
            compilerGUI.getCheckButton().setEnabled(false);
        }
        compilerGUI.getExtractButton().setEnabled(true);
    }
}
