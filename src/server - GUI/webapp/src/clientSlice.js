// clientSlice.js

import { createSlice } from '@reduxjs/toolkit';

const clientSlice = createSlice({
    name: 'client',
    initialState: {
        clients: [],
        tabs: [{ id: 0, name: "Home", default: true }],
        selectedClient: null
    },
    reducers: {
        setClients: (state, action) => {
            state.clients = action.payload;
        },
        addClientTab: (state, action) => {
            if (!state.tabs.some(tab => tab.id === action.payload.id)) {
                state.tabs.push(action.payload);
            }
        },
        selectClient: (state, action) => {
            state.selectedClient = action.payload;
        }
    }
});

export const { setClients, addClientTab, selectClient } = clientSlice.actions;
export default clientSlice.reducer;
