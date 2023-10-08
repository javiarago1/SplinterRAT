import {DELETE, DOWNLOAD, MOVE, RUN} from "../actions/fileManagerActions";
import {addProgressBar} from "@redux/slices/fileManagerSlice";

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

export const handleDownload = async (websocket, store, action) => {
    let jsonResponse = null;
    try {
        const response = await fetch('http://localhost:3055/create-byte-channel', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                client_id: store.getState().client.selectedClient.systemInformation.UUID
            })
        });
       jsonResponse = await response.json();
       console.log(jsonResponse)
    } catch (error) {
        console.error(error);
    }
    if (jsonResponse == null) return;
    store.dispatch(addProgressBar({ channel_id: jsonResponse.channel_id }));
    if (websocket && jsonResponse) {
        const message = {
            ACTION: DOWNLOAD,
            from_path: action.payload,
            channel_id: jsonResponse.channel_id,
            client_id: store.getState().client.selectedClient.systemInformation.UUID
        };
        websocket.send(JSON.stringify(message));
        console.log("Download to " + action.payload);
    }
}