package Builder.Workers;

import javax.swing.*;
import java.nio.file.Path;

import Builder.CompilerGUI;
import Compiler.CompilerProcess;

public class CompileWorker extends SwingWorker<Void,Void> {

    private final CompilerGUI compilerGUI;
    private final String compileCommand;
    private final String assemblyCommand;
    private final Path pathOfClientFiles;
    private int result;

    public CompileWorker(CompilerGUI compilerGUI, String compileCommand, String assemblyCommand, Path pathOfClientFiles){
        this.compilerGUI = compilerGUI;
        this.compileCommand = compileCommand;
        this.assemblyCommand = assemblyCommand;
        this.pathOfClientFiles = pathOfClientFiles;
    }

    @Override
    protected Void doInBackground() {
        CompilerProcess compilerProcess = new CompilerProcess(compileCommand, assemblyCommand, pathOfClientFiles);
        result = compilerProcess.call();
        return null;
    }

    @Override
    protected void done() {
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
