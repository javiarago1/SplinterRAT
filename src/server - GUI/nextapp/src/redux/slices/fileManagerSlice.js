import {createSlice} from "@reduxjs/toolkit";

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
            console.log("Directory data")
            console.log(newDirectoryData)

            const existingIndex = state.directoryStack.length - 1;
            console.log("comparisson")
            console.log(state.currentDirectory)
            console.log(newDirectoryData.requested_directory )
            if (state.currentDirectory === newDirectoryData.requested_directory) {
                state.directoryStack[existingIndex].folders = newDirectoryData.folders;
                state.directoryStack[existingIndex].files = newDirectoryData.files;
                state.directoryStack[existingIndex].visited = false;
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
            state.currentDirectory = state.directoryStack[state.directoryStack.length - 1].requested_directory;
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
            state.clipboard.from_paths = action.payload.from_paths.map(path => state.currentDirectory + path.name);
            state.selectedRows = [];
        },
        clearClipboard: (state) => {
            state.clipboard = {
                action: null,
                from_paths: [],
            };
        },
        deleteFilesFromTable: (state, action) => {
            const itemsToDelete = action.payload;
            const directory = state.directoryStack[state.directoryStack.length - 1];
            itemsToDelete.forEach(item => {
                if(item.type === 'file') {
                    const fileIndex = directory.files.findIndex(file => file.name === item.name);
                    if(fileIndex !== -1) {
                        directory.files.splice(fileIndex, 1);
                    }
                }
                else if(item.type === 'folder') {
                    const folderIndex = directory.folders.indexOf(item.name);
                    if(folderIndex !== -1) {
                        directory.folders.splice(folderIndex, 1);
                    }
                }
            });

        }

    }
});

export const {
    reorderDisks,
    setDisks,
    setDirectoryData,
    popDirectory,
    selectRow,
    clearSelectedRows ,
    deselectRow,
    setClipboard,
    clearClipboard,
    deleteFilesFromTable
}  = fileManagerSlice.actions;


export const fileManagerReducer = fileManagerSlice.reducer;