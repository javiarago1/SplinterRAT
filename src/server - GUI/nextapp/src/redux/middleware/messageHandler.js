import {addSingleClientToTable, selectClientByUUID, setClients} from "@redux/slices/clientSlice";
import {setDirectoryData, setDisks, updateProgressBar} from "@redux/slices/fileManagerSlice";
import {setWebcamDevices, setWebcamFrame} from "@redux/slices/webcamManagerSlice";

export const handleWebSocketMessage = (store, data) => {
    console.log("Received message")
    console.log(data)
    if (data.RESPONSE === "TABLE_INFO") {
        store.dispatch(setClients(data.content));
    } else if (data.RESPONSE === "CLIENT_SET"){
        store.dispatch(selectClientByUUID(data));
    } else if (data.RESPONSE === "SYS_NET_INFO"){
        store.dispatch(addSingleClientToTable(data));
    } else if (data.RESPONSE === "DISKS") {
        store.dispatch(setDisks(data.disks));
        if (data.firstDiskDirectory) {
            store.dispatch(setDirectoryData(data.firstDiskDirectory));
        }
    } else if (data.RESPONSE === "DIRECTORY") {
        store.dispatch(setDirectoryData(data.directory));
    } else if (data.RESPONSE === "PROGRESS_BAR"){
        store.dispatch(updateProgressBar(data));
    } else if (data.RESPONSE === "WEBCAM_DEVICES"){
        store.dispatch(setWebcamDevices(data))
    }
};

export const handleWebSocketBinary = (store, data) => {
    store.dispatch(setWebcamFrame(data));
};