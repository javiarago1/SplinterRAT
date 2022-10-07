package GUI.TableUtils.FileBrowser.TreeInterpreter.TreeGUI;


import Connections.Streams;
import GUI.Main;
import Information.Action;


import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Arrays;


public class DiskMenu implements Runnable {
    private final JMenu browserMenu;
    private final Streams stream;
    private String[] disksArray;
    private final String[] defaultPaths = new String[]{"Downloads\\", "Documents\\", "Desktop\\", "Pictures\\", "Music\\", "Videos\\"};
    private final String userPath;
    private final String diskWindows;


    public DiskMenu(JMenu browserMenu, Streams stream) {
        this.browserMenu = browserMenu;
        this.stream = stream;
        userPath = stream.getTempSystemInformation().HOME_PATH();
        diskWindows = stream.getTempSystemInformation().HOME_DRIVE();
    }

    public void createMenu() {
        createFileBrowserOptions();
    }


    protected ActionListener generateActionListener(String path) {
        return e -> new TreeGUI(path, stream, Main.gui.getFrame());
    }

    private void createFileBrowserOptions() {
        System.out.println("Array de discos -> " + Arrays.toString(disksArray));
        for (String s : disksArray) {
            if (s.equals(diskWindows + "\\")) {
                JMenu windowsDiskMenu = new JMenu(stream.getTempSystemInformation().OPERATING_SYSTEM());
                for (String e : defaultPaths) {
                    e = e.substring(0, e.length() - 1);
                    JMenuItem item = new JMenuItem(e);
                    item.addActionListener(generateActionListener(diskWindows + userPath + "\\" + e));
                    windowsDiskMenu.add(item);
                }
                browserMenu.add(windowsDiskMenu);
            }
            JMenuItem tempMenuItem = new JMenuItem(s);
            tempMenuItem.addActionListener(generateActionListener(s));
            browserMenu.add(tempMenuItem);
        }

    }

    public Streams getStream() {
        return stream;
    }

    public String[] requestDisks() {
        return (String[]) stream.sendAndReadJSON(Action.DISK);
    }

    @Override
    public void run() {
        disksArray = requestDisks();
        createMenu();
    }
}
