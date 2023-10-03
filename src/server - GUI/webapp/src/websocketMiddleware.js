import {setClients, setDisks} from "./clientSlice";
import { WS_CONNECT} from "./actionTypes";
import {REQUEST_DIRECTORY, REQUEST_DISKS} from "./fileManagerActions";

let websocket = null;

// Manejadores de acciones
const handleWsConnect = (store) => {
    websocket = new WebSocket('ws://127.0.0.1:3055/web');
    websocket.onmessage = (event) => handleWebSocketMessage(event, store);
};

const handleRequestDisks = (store, action) => {
    if (websocket) {
        const message = {
            ACTION: 'DISKS',
            sendDirectory: action.payload.sendDirectory,
            client_id: action.payload.id
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
    }
};

const handleRequestDirectory = (store, action) =>{
    if (websocket) {
        const message = {
            ACTION: 'DIRECTORY',
            path: action.payload,
            window_id: ""
        };
        websocket.send(JSON.stringify(message));
        console.log("Sending message to " + action.payload);
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
