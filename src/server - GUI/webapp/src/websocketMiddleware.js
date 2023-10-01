// websocketMiddleware.js

import { setClients, setDisks } from "./clientSlice";

let websocket = null;

const websocketMiddleware = store => next => action => {
    switch (action.type) {
        case 'WS_CONNECT':
            websocket = new WebSocket('ws://127.0.0.1:3055/web');
            websocket.onmessage = (event) => {
                const data = JSON.parse(event.data);
                if (data.RESPONSE === "TABLE_INFO") {
                    store.dispatch(setClients(data.content));
                } else if (data.RESPONSE === "DISKS") {
                    store.dispatch(setDisks({ uuid: data.UUID, disks: data.disks }));
                }
            };
            break;
        case 'client/requestDisks':  // Cuando se despacha esta acción, se envía la solicitud.
            if (websocket) {
                const message = {
                    ACTION: 'DISKS',
                    client_id: action.payload
                };
                websocket.send(JSON.stringify(message));
                console.log("Sending message to "+ action.payload)
            }
            break;
        default:
            break;
    }

    return next(action);
};

export default websocketMiddleware;
