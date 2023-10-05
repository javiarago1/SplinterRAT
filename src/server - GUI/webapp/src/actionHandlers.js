import {DELETE, MOVE, RUN} from "./fileManagerActions";

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

export const handleMove = (websocket, store, action) =>{
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

export const handleDelete = (websocket, store, action) =>{
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

export const handleRun = (websocket, store, action) =>{
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
