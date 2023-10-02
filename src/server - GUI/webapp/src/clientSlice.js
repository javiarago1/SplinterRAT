import { createSlice } from '@reduxjs/toolkit';

const clientSlice = createSlice({
    name: 'client',
    initialState: {
        clients: [],
        selectedClient: null,
        disks: [], // Un arreglo simple para los discos del cliente seleccionado
    },
    reducers: {
        setClients: (state, action) => {
            state.clients = action.payload;
        },
        selectClient: (state, action) => {
            state.selectedClient = action.payload;
            state.disks = []; // Reseteamos los discos cuando seleccionamos un nuevo cliente
        },
        reorderDisks: (state, action) => {
            const selectedDisk = action.payload;
            state.disks = [selectedDisk, ...state.disks.filter(disk => disk !== selectedDisk)];
        },
        requestDisks: (state, action) => {},
        setDisks: (state, action) => {
            state.disks = action.payload; // Establecemos los discos directamente
        }
    }
});

export const { setClients, selectClient, requestDisks, setDisks, reorderDisks } = clientSlice.actions;
export default clientSlice.reducer;
