import {createSlice} from "@reduxjs/toolkit";

const screenManagerSlice = createSlice({
    name: 'webcamManager',
    initialState: {
        screens: [],
        frame: null,
        isScreenOn: false,
        originalDimensions: [],
        canControl: false,
    },
    reducers: {
        setScreens: (state, action) => {
            state.screens = action.payload.list_of_monitors;
        },
        setScreenFrame: (state, action) => {
            const newBlob = new Blob([action.payload], {type: 'image/jpeg'});
            if (state.frame) {
                URL.revokeObjectURL(state.frame);
            }
            state.frame = URL.createObjectURL(newBlob);
        },
        setScreenState: (state, action) => {
            state.isScreenOn = action.payload;
        },
        reorderScreens: (state, action) => {
            const selectedDevice = action.payload;
            state.screens = [selectedDevice, ...state.screens.filter(device => device !== selectedDevice)];
        },
        setOriginalDimensions: (state, action) => {
            state.originalDimensions = action.payload.dimensions;
        },
        setCanControl: (state, action) => {
            state.canControl = action.payload;
        }
    }
});

export const {
    setScreens,
    setScreenFrame,
    setScreenState,
    reorderScreens,
    setOriginalDimensions,
    setCanControl
} = screenManagerSlice.actions;


export const screenManagerReducer = screenManagerSlice.reducer;