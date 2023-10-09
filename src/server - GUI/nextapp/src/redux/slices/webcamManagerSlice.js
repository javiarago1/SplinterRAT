import {createSlice} from "@reduxjs/toolkit";

const webcamManagerSlice = createSlice({
    name: 'webcamManager',
    initialState: {
        devices: [],
        frame: [],
        isWebcamOn: false,
        isRecording: false,
    },
    reducers: {
        setDevices: (state, action) => {
            const selectedDisk = action.payload;
        },
        setWebcamFrame: (state, action) => {
            state.frame = action.payload;
        },
    }
});

export const {
    setDevices,
    setWebcamFrame
}  = webcamManagerSlice.actions;


export const webcamManagerReducer = webcamManagerSlice.reducer;