package GUI.Compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class IncludeModifier {

    private final String location;

    public IncludeModifier(String location) {
        this.location = location;
    }


    public void addInclude(String includeTarget) {
        File fileToBeModified = new File(location);

        StringBuilder oldContent = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileToBeModified))) {
            String line = reader.readLine();
            System.out.println(line);
            while (line != null) {
                oldContent.append(line).append(System.lineSeparator());
                line = reader.readLine();
            }
            String newContent = oldContent.toString().replaceAll("//" + includeTarget, includeTarget);
            FileWriter writer = new FileWriter(fileToBeModified);
            writer.write(newContent);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void removeInclude(String includeTarget) {
        File fileToBeModified = new File(location);
        StringBuilder oldContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileToBeModified))) {
            String line = reader.readLine();
            while (line != null) {
                oldContent.append(line).append(System.lineSeparator());
                line = reader.readLine();
            }
            String newContent = oldContent.toString().replaceAll(includeTarget, "//" + includeTarget);
            FileWriter writer = new FileWriter(fileToBeModified);
            writer.write(newContent);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
