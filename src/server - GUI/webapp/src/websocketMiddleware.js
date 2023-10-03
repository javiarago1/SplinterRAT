import {setClients, setDisks, setDirectoryData } from "./clientSlice";
import { WS_CONNECT} from "./actionTypes";
import {REQUEST_DIRECTORY, REQUEST_DISKS} from "./fileManagerActions";

let websocket = null;

// Manejadores de acciones
const handleWsConnect = (store) => {
    websocket = new WebSocket('ws://127.0.0.1:3055/web');
    websocket.onmessage = (event) => handleWebSocketMessage(event, store);
};

const handleRequestDisks = (store, action) => {
    console.log(action);
    if (websocket) {
        const message = {
            ACTION: 'DISKS',
            sendDirectory: action.payload.sendDirectory,
            client_id: action.payload.client_id
        };
        websocket.send(JSON.stringify(message));
        console.log("Sending message to " + action.payload);
    }
};

// Función para manejar mensajes de WebSocket
const handleWebSocketMessage = (event, store) => {
    const data = JSON.parse(event.data);
    console.log(data);
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

const handleRequestDirectory = (store, action) =>{
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

// Mapeo de tipos de acción a manejadores
const actionHandlers = {
    [WS_CONNECT]: handleWsConnect,
    [REQUEST_DISKS]: handleRequestDisks,
    [REQUEST_DIRECTORY]: handleRequestDirectory
};

// Middleware
const websocketMiddleware = store => next => action => {
    const handler = actionHandlers[action.type];
    if (handler) handler(store, action);
    return next(action);
};

export default websocketMiddleware;
