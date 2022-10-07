package GUI.TableUtils.FileBrowser.TreeInterpreter.TreeUtils;

import Connections.Streams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class Manager implements Runnable {
    private List<String> filesArray;
    private File[] uploadArray;
    private final Streams stream;
    private List<String> directories;
    private String directory;

    public Manager(List<String> filesArray, List<String> directories, Streams stream) {
        this.filesArray = new ArrayList<>(filesArray);
        this.directories = new ArrayList<>(directories);
        this.stream = stream;
    }

    public Manager(List<String> filesArray, String directory, Streams stream) {
        this.filesArray = new ArrayList<>(filesArray);
        this.directory = directory;
        this.stream = stream;
    }

    public Manager(List<String> filesArray, Streams stream) {
        this.filesArray = new ArrayList<>(filesArray);
        this.stream = stream;
    }

    public Manager(File[] uploadArray, List<String> filesArray, Streams stream) {
        this.filesArray = filesArray;
        this.uploadArray = uploadArray;
        this.stream = stream;
    }

    @Override
    public abstract void run();

    public List<String> getFilesArray() {
        return filesArray;
    }

    public List<String> getDirectories() {
        return directories;
    }

    public String getDirectory() {
        return directory;
    }

    public Streams getStream() {
        return stream;
    }

    public File[] getUploadArray() {
        return uploadArray;
    }
}
