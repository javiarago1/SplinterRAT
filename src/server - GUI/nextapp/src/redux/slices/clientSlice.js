import { createSlice } from '@reduxjs/toolkit';

const clientSlice = createSlice({
    name: 'client',
    initialState: {
        clients: [],
        selectedClient: null,
    },
    reducers: {
        setClients: (state, action) => {
            state.clients = action.payload;
        },
        selectClient: (state, action) => {
            state.selectedClient = action.payload;
        },
        addSingleClientToTable: (state, action) => {
            state.clients.push(action.payload);
        }
    }
});


export const { setClients, selectClient, addSingleClientToTable } = clientSlice.actions;

export const clientReducer = clientSlice.reducer;


