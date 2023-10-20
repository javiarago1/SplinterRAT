package Compiler;

import Server.ConnectionStore;


import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;


public class CompilerProcess implements Callable<Integer> {
    private final String compileCommand;
    private final Path pathOfClients;

    public CompilerProcess(String compileCommand, Path pathOfClients) {
        this.compileCommand = compileCommand;
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
    public Integer call() {
        String[] OSCommand = VersionChecker.getOSProperCommand();
        ProcessBuilder compileProcess = new ProcessBuilder();
        compileProcess.command(OSCommand[0], OSCommand[1], compileCommand).directory(pathOfClients.toFile());
        int result = executeProcess(compileProcess);
        if (result != 0) {
            return -2;
        } else {
            return 0;
        }
    }
}
