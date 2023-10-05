// store.js

import { configureStore } from '@reduxjs/toolkit';
import websocketMiddleware from '../middleware/websocketMiddleware';
import { clientReducer } from "../slices/clientSlice";
import { fileManagerReducer} from "../slices/fileManagerSlice";


const rootReducer = {
    client: clientReducer,
    fileManager: fileManagerReducer,
};
 export const store = configureStore({
     reducer: rootReducer,
     middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(websocketMiddleware),
});
