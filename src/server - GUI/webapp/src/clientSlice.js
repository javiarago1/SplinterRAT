import { createSlice } from '@reduxjs/toolkit';

const fileManagerSlice = createSlice({
    name: 'fileManager',
    initialState: {
        disks: [],
        directoryStack: [],
        currentDirectory: null,
        selectedRows: [],
        clipboard: {
            action: null,
            from_paths: [],
        }
    },
    reducers: {
        reorderDisks: (state, action) => {
            const selectedDisk = action.payload;
            state.disks = [selectedDisk, ...state.disks.filter(disk => disk !== selectedDisk)];
            state.directoryStack = [];
        },
        setDisks: (state, action) => {
            state.disks = action.payload;
        },
        setDirectoryData: (state, action) => {
            const newDirectoryData = action.payload;
            console.log(newDirectoryData)
            const existingIndex = state.directoryStack.findIndex(
                dir => dir.requested_directory === newDirectoryData.requested_directory
            );
            if (existingIndex !== -1) {
                console.log("already")
                state.directoryStack[existingIndex].folders = newDirectoryData.folders;
                state.directoryStack[existingIndex].files = newDirectoryData.files;
                state.directoryStack[existingIndex].visited = false;
                state.currentDirectory = newDirectoryData.requested_directory;
            } else {
                state.directoryStack.push(newDirectoryData);
                state.currentDirectory = newDirectoryData.requested_directory;
            }
            if (state.directoryStack.length > 1) {
                state.directoryStack[state.directoryStack.length - 2].visited = true;
            }
        },
        popDirectory: (state) => {
            state.selectedRows = [];
            state.directoryStack.pop();
        },
        selectRow: (state, action) => {
            state.selectedRows.push(action.payload);
        },
        deselectRow: (state, action) => {
            state.selectedRows = state.selectedRows.filter(
                row => row.name !== action.payload.name || row.type !== action.payload.type
            );
        },
        clearSelectedRows: (state) => {
            state.selectedRows = [];
        },
        setClipboard: (state, action) => {
            state.clipboard.action = action.payload.action;
            state.clipboard.from_paths = action.payload.from_paths.map(path => state.currentDirectory + (state.directoryStack.length > 1 ? "\\" : "")  + path.name);
            state.selectedRows = [];
        },
        clearClipboard: (state) => {
            state.clipboard = {
                action: null,
                from_paths: [],
            };
        },

    }
});


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
        }
    }
});

//... tus slices aquí ...

export const { setClients, selectClient } = clientSlice.actions;
export const {
    reorderDisks,
    setDisks,
    setDirectoryData,
    popDirectory,
    selectRow,
    clearSelectedRows ,
    deselectRow,
    setClipboard,
    clearClipboard
}  = fileManagerSlice.actions;

export const clientReducer = clientSlice.reducer;
export const fileManagerReducer = fileManagerSlice.reducer;

