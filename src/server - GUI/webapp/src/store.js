// store.js

import { configureStore } from '@reduxjs/toolkit';
import websocketMiddleware from './websocketMiddleware';
import { clientReducer, fileManagerReducer } from "./clientSlice";


const rootReducer = {
    client: clientReducer,
    fileManager: fileManagerReducer,
};
 export const store = configureStore({
     reducer: rootReducer,
     middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(websocketMiddleware),
});
