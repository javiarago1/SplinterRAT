import {createSlice} from '@reduxjs/toolkit';

const clientSlice = createSlice({
    name: 'client',
    initialState: {
        clients: {},
        selectedClient: null,
    },
    reducers: {
        setClients: (state, action) => {
            Object.values(action.payload).forEach(client => {
                const clientId = client.systemInformation.UUID;
                state.clients[clientId] = client;
                state.clients[clientId].isConnected = true;
            });
        },
        selectClient: (state, action) => {
            state.selectedClient = action.payload;
        },
        selectClientByUUID: (state, action) => {
            state.selectedClient = state.clients[action.payload.client_id];
        },
        removeSelectedClient: (state, action) => {
            state.selectedClient = null;
        },
        disconnectClient: (state, action) => {
            const clientId = action.payload.client_id;
            const client = state.clients[clientId];
            if (client) {
                client.isConnected = false;
            }
            if (state.selectedClient && state.selectedClient.systemInformation.UUID === clientId) {
                state.selectedClient = null;
            }
        }

    }
});

export const {setClients,
    selectClient,
    selectClientByUUID,
    removeSelectedClient,
    disconnectClient
} = clientSlice.actions;

export const clientReducer = clientSlice.reducer;
