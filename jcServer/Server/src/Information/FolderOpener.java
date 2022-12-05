package Information;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FolderOpener {
    public static void open(String path){
        try {
            Desktop.getDesktop().open(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
