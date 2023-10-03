import { createSlice } from '@reduxjs/toolkit';

const clientSlice = createSlice({
    name: 'client',
    initialState: {
        clients: [],
        selectedClient: null,
        disks: [],
    },
    reducers: {
        setClients: (state, action) => {
            state.clients = action.payload;
        },
        selectClient: (state, action) => {
            state.selectedClient = action.payload;
            state.disks = [];
        },
        reorderDisks: (state, action) => {
            const selectedDisk = action.payload;
            state.disks = [selectedDisk, ...state.disks.filter(disk => disk !== selectedDisk)];
        },
        setDisks: (state, action) => {
            state.disks = action.payload;
        }
    }
});

export const { setClients, selectClient, reorderDisks, setDisks } = clientSlice.actions;
export default clientSlice.reducer;
