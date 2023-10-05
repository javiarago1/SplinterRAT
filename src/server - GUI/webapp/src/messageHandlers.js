import { setClients } from "./clientSlice";
import {setDirectoryData, setDisks} from "./fileManagerSlicer";

export const handleWebSocketMessage = (store, data) => {
    if (data.RESPONSE === "TABLE_INFO") {
        store.dispatch(setClients(data.content));
    } else if (data.RESPONSE === "DISKS") {
        store.dispatch(setDisks(data.disks));
        if (data.firstDiskDirectory) {
            store.dispatch(setDirectoryData(data.firstDiskDirectory));
        }
    } else if (data.RESPONSE === "DIRECTORY") {
        store.dispatch(setDirectoryData(data.directory));
    }
};