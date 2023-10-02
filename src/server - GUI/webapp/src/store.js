// store.js

import { configureStore } from '@reduxjs/toolkit';
import websocketMiddleware from './websocketMiddleware';
import clientReducer from './clientSlice';

 export const store = configureStore({
    reducer: {
        client: clientReducer,
    },
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(websocketMiddleware),
});
