import { configureStore } from '@reduxjs/toolkit';
import clientReducer from './clientSlice';

export const store = configureStore({
    reducer: {
        client: clientReducer,
    },
});
