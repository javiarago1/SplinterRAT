// store.js

import { configureStore } from '@reduxjs/toolkit';
import websocketMiddleware from '../middleware/websocketMiddleware';
import { clientReducer } from "../slices/clientSlice";
import { fileManagerReducer} from "../slices/fileManagerSlice";
import {setWebcamFrame, webcamManagerReducer} from "@redux/slices/webcamManagerSlice";
import {screenManagerReducer, setScreenFrame} from "@redux/slices/screenManagerSlice";
import screenManager from "@components/Funcionalities/ScreenManager/ScreenManager";
import {reverseShellReducer} from "@redux/slices/revereShellSlice";
import {credentialsManagerReducer} from "@redux/slices/credentialsManagerSlice";

const rootReducer = {
    client: clientReducer,
    fileManager: fileManagerReducer,
    webcamManager: webcamManagerReducer,
    screenManager: screenManagerReducer,
    reverseShell: reverseShellReducer,
    credentialsManager: credentialsManagerReducer,
};
export const store = configureStore({
    reducer: rootReducer,
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            serializableCheck: {
                ignoredActions: [setWebcamFrame.type, setScreenFrame.type],
            },
        }).concat(websocketMiddleware),
});
