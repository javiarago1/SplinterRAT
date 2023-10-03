import { createSlice } from '@reduxjs/toolkit';

const clientSlice = createSlice({
    name: 'client',
    initialState: {
        clients: [],
        selectedClient: null,
        disks: [],
        directoryStack: [],
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
            state.directoryStack = [];
        },
        setDisks: (state, action) => {
            state.disks = action.payload;
        },
        setDirectoryData: (state, action) => {
            state.directoryStack.push(action.payload);
            if (state.directoryStack.length > 1) state.directoryStack[state.directoryStack.length - 2].visited = true;
        },
        popDirectory: (state) => {
            state.directoryStack.pop();
            state.directoryStack.pop();
        },
    }
});

export const { setClients, selectClient, reorderDisks, setDisks, setDirectoryData,popDirectory } = clientSlice.actions;
export default clientSlice.reducer;
