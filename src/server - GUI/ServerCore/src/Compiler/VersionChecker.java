package Compiler;

import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class VersionChecker {

    static String[] getOSProperCommand() {
        String shell = "";
        String typeOfOpening = "";
        if (SystemUtils.IS_OS_WINDOWS) {
            shell = "cmd.exe";
            typeOfOpening = "/c"; // /c
        } else if (SystemUtils.IS_OS_LINUX) {
            shell = "/bin/bash";
            typeOfOpening = "-c";
        }
        return new String[]{shell, typeOfOpening};
    }

    public static int checkVersion(String utilities) {
        String[] commandArray = VersionChecker.getOSProperCommand();
        ProcessBuilder processBuilder = new ProcessBuilder();
        String pathOfUtilities = "";
        if (!utilities.equals("g++ / windres")) pathOfUtilities = utilities + "\\";
        System.out.println(pathOfUtilities);
        processBuilder.command(commandArray[0], commandArray[1], pathOfUtilities + "g++ -dumpversion");
        try {

            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println(output);
                int finalPositionOfVersion = output.indexOf(".") == -1 ? output.length() : output.indexOf(".");
                if (Integer.parseInt(output.substring(0, finalPositionOfVersion).replaceAll("[\n\r]", "")) >= 9) {
                    return 0;
                } else {
                    return -1;
                }
            } else {
                return -2;
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return -2;
    }
}
