import {createSlice} from '@reduxjs/toolkit';

const clientSlice = createSlice({
    name: 'client',
    initialState: {
        clients: {},
        selectedClient: null,
    },
    reducers: {
        setClients: (state, action) => {
            state.clients = action.payload;
        },
        selectClient: (state, action) => {
            state.selectedClient = action.payload;
        },
        selectClientByUUID: (state, action) => {
            state.selectedClient = state.clients[action.payload.client_id];
        },
        addSingleClientToTable: (state, action) => {
            const client = action.payload;
            state.clients[client.systemInformation.UUID] = client;
        },
        removeSelectedClient: (state, action) => {
            state.selectedClient = null;
        }
    }
});

export const {setClients, selectClient, addSingleClientToTable, selectClientByUUID, removeSelectedClient} = clientSlice.actions;

export const clientReducer = clientSlice.reducer;
