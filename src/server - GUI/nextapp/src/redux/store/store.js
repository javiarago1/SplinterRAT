// store.js

import { configureStore } from '@reduxjs/toolkit';
import websocketMiddleware from '../middleware/websocketMiddleware';
import { clientReducer } from "../slices/clientSlice";
import { fileManagerReducer} from "../slices/fileManagerSlice";
import {setWebcamFrame, webcamManagerReducer} from "@redux/slices/webcamManagerSlice";


const rootReducer = {
    client: clientReducer,
    fileManager: fileManagerReducer,
    webcamManager: webcamManagerReducer
};
export const store = configureStore({
    reducer: rootReducer,
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware({
            serializableCheck: {
                ignoredActions: [setWebcamFrame.type],
            },
        }).concat(websocketMiddleware),
});
