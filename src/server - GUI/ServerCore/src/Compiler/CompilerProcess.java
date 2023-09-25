package Compiler;

import Server.ConnectionStore;


import java.io.IOException;
import java.nio.file.Path;


public class CompilerProcess implements Runnable {


    private final String compileCommand;
    private final String assemblyCommand;
    private final Path pathOfClients;

    public CompilerProcess(String compileCommand, String assemblyCommand, Path pathOfClients) {
        this.compileCommand = compileCommand;
        this.assemblyCommand = assemblyCommand;
        this.pathOfClients = pathOfClients;
    }

    private int executeProcess(ProcessBuilder processBuilder) {
        try {
            Process process = processBuilder.start();
            return process.waitFor();
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return -1;

    }

    @Override
    public void run() {
        String[] OSCommand = VersionChecker.getOSProperCommand();
        ProcessBuilder assemblyProcess = new ProcessBuilder();
        assemblyProcess.command(OSCommand[0], OSCommand[1], assemblyCommand).directory(Path.of(pathOfClients.toString(), "compile_configuration").toFile());
        int result = executeProcess(assemblyProcess);
        if (result != 0) {
            //ConnectionStore.updaterInterface.showResultOfCompilation(-1);
            return;
        }
        ProcessBuilder compileProcess = new ProcessBuilder();
        compileProcess.command(OSCommand[0], OSCommand[1], compileCommand).directory(pathOfClients.toFile());
        result = executeProcess(compileProcess);
        if (result != 0) {
            //ConnectionStore.updaterInterface.showResultOfCompilation(-2);
        } else {
            //ConnectionStore.updaterInterface.showResultOfCompilation(0);;
        }
    }
}
