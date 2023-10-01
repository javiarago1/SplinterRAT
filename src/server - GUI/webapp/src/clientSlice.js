// clientSlice.js

import { createSlice } from '@reduxjs/toolkit';

const clientSlice = createSlice({
    name: 'client',
    initialState: {
        clients: [],
        tabs: [{ uuid: 0, name: "Home", default: true }],
        selectedClient: null,
        disks: {}, // uuid: disks[]
    },
    reducers: {
        setClients: (state, action) => {
            state.clients = action.payload;
        },
        addClientTab: (state, action) => {
            if (!state.tabs.some(tab => tab.uuid === action.payload.uuid)) {
                state.tabs.push(action.payload);
            }
        },
        selectClient: (state, action) => {
            state.selectedClient = action.payload;
        },
        requestDisks: (state, action) => {

        },
        setDisks: (state, action) => {
            state.disks[action.payload.uuid] = action.payload.disks;
        }
    }
});

export const { setClients, addClientTab, selectClient, requestDisks, setDisks } = clientSlice.actions;
export default clientSlice.reducer;
