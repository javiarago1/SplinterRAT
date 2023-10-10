import {createSlice} from "@reduxjs/toolkit";

const webcamManagerSlice = createSlice({
    name: 'webcamManager',
    initialState: {
        webcamDevices: [],
        frame: null,
        isWebcamOn: false,
        isRecording: false,
    },
    reducers: {
        setWebcamDevices: (state, action) => {
            state.webcamDevices = action.payload.list_of_webcams;
        },
        setWebcamFrame: (state, action) => {
            const newBlob = new Blob([action.payload], { type: 'image/jpeg' });
            if (state.frame) {
                URL.revokeObjectURL(state.frame);
            }
            state.frame = URL.createObjectURL(newBlob);
        },
        reorderWebcamDevices: (state, action) => {
            const selectedDevice = action.payload;
            state.webcamDevices = [selectedDevice, ...state.webcamDevices.filter(device => device !== selectedDevice)];
        },
    }
});

export const {
    setWebcamDevices,
    setWebcamFrame,
    reorderWebcamDevices
}  = webcamManagerSlice.actions;


export const webcamManagerReducer = webcamManagerSlice.reducer;