package Utilities;

import TableUtils.FileManager.FileManagerGUI;
import Utils.UniqueByteIDGenerator;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class WindowHandler <T extends AbstractMWDialogCreator>{
    private final UniqueByteIDGenerator uniqueByteIDGenerator = new UniqueByteIDGenerator();
    private final ConcurrentHashMap<Byte, T> windowManagerMAP = new ConcurrentHashMap<>();

    public byte createInstanceAndGetID(T gui){
        byte id =  uniqueByteIDGenerator.getID();
        gui.setWindowId(id);
        windowManagerMAP.put(id,gui);
        return id;
    }


    public void finishInstance(byte id){
        windowManagerMAP.remove(id).closeDialog();
        uniqueByteIDGenerator.finishTask(id);
        System.out.println("Finished window id: "+id);
        System.out.println("Map left: "+windowManagerMAP.size());
    }

    public T getWindow(byte id){
        return windowManagerMAP.get(id);
    }


}
