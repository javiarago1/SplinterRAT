import {REQUEST_DISKS, REQUEST_DIRECTORY, COPY, MOVE, DELETE, RUN, DOWNLOAD} from "../actions/fileManagerActions";
import {
    handleRequestDisks,
    handleCopy,
    handleMove,
    handleDelete,
    handleRun,
    handleRequestDirectory,
    handleDownload,
    handleStartWebcam, handleSelectClient
} from './actionHandlers';
import { handleWebSocketMessage } from './messageHandler';
import {SELECT_CLIENT, WS_CONNECT} from "../actions/connectionActions";
import {START_WEBCAM} from "@redux/actions/webcamManagerActions";

let websocket = null;

const actionHandlers = {
    [WS_CONNECT]: (store) => {
        websocket = new WebSocket('ws://127.0.0.1:3055/web');
        websocket.onmessage = (event) => {
            const data = JSON.parse(event.data);
            handleWebSocketMessage(store, data);
        };
    },
    [REQUEST_DISKS]: (store, action) => handleRequestDisks(websocket, store, action),
    [REQUEST_DIRECTORY]: (store, action) => handleRequestDirectory(websocket, store, action),
    [COPY]: (store, action) => handleCopy(websocket, store, action),
    [MOVE]: (store, action) => handleMove(websocket, store, action),
    [DELETE]: (store, action) => handleDelete(websocket, store, action),
    [RUN]: (store, action) => handleRun(websocket, store, action),
    [DOWNLOAD]: (store, action) => handleDownload(websocket, store, action),
    [START_WEBCAM]: (store, action) => handleStartWebcam(websocket, store, action),
    [SELECT_CLIENT]: (store, action) => handleSelectClient(websocket, store, action),

};

const websocketMiddleware = store => next => action => {
    const handler = actionHandlers[action.type];
    if (handler) handler(store, action);
    return next(action);
};

export default websocketMiddleware;
