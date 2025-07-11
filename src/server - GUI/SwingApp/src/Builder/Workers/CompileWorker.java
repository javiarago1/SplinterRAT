package Builder.Workers;

import javax.swing.*;
import java.nio.file.Path;

import Builder.Animations.CompilingAnimationDialog;
import Builder.CompilerGUI;
import Compiler.CompilerProcess;

public class CompileWorker extends SwingWorker<Void,Void> {

    private final CompilerGUI compilerGUI;
    private final String compileCommand;
    private final Path pathOfClientFiles;

    private final CompilingAnimationDialog compilingAnimationDialog;
    private int result;

    public CompileWorker(CompilerGUI compilerGUI, String compileCommand, Path pathOfClientFiles){
        this.compilerGUI = compilerGUI;
        this.compileCommand = compileCommand;
        this.pathOfClientFiles = pathOfClientFiles;
        compilingAnimationDialog = new CompilingAnimationDialog(compilerGUI);
        compilingAnimationDialog.startDialog();
    }

    @Override
    protected Void doInBackground() {
        CompilerProcess compilerProcess = new CompilerProcess(compileCommand, pathOfClientFiles);
        result = compilerProcess.call();
        return null;
    }

    @Override
    protected void done() {
        compilingAnimationDialog.closeDialog();
        switch (result){
            case -1 -> JOptionPane.showMessageDialog(compilerGUI,
                    "Error assembling client, check for windres and try again.",
                    "Error assembling", JOptionPane.ERROR_MESSAGE);
            case -2 -> JOptionPane.showMessageDialog(compilerGUI,
                    "Error compiling client, check your compiler and try again.",
                    "Error compiling", JOptionPane.ERROR_MESSAGE);
            case 0 -> JOptionPane.showMessageDialog(
                    compilerGUI,
                    "Client compiled successfully!",
                    "Compiler information",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
