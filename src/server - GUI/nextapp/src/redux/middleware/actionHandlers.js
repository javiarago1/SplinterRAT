import {DELETE, DOWNLOAD, MOVE, RUN} from "../actions/fileManagerActions";
import {addProgressBar} from "@redux/slices/fileManagerSlice";
import {START_WEBCAM, WEBCAM_DEVICES} from "@redux/actions/webcamManagerActions";
import {SELECT_CLIENT} from "@redux/actions/connectionActions";

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
        return jsonResponse.channel_id || null;
    } catch (error) {
        console.error(error);
        return null;
    }
};

export const handleStartWebcam = async (websocket, store, action) => {
    const channelId = await fetchChannelId(store, "WEBCAM_STREAMING");
    if (channelId === null) {
        console.error("Failed to retrieve channel ID.");
        return;
    }
    if (websocket) {
        const message = {
            ACTION: START_WEBCAM,
            channel_id: channelId,
            selected_device: action.payload.selected_device,
            is_fragmented: false,
            fps: 30,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Start webcam to " + action.payload);
    }
}

export const handleDownload =  async (websocket, store, action) => {
    const channelId = await fetchChannelId(store, "ZIP_FILE");

    if (channelId === null) {
        console.error("Failed to retrieve channel ID.");
        return;
    }
    store.dispatch(addProgressBar({channel_id: channelId}));
    if (websocket) {
        const message = {
            ACTION: DOWNLOAD,
            from_path: action.payload,
            channel_id: channelId,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Download to " + action.payload);
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