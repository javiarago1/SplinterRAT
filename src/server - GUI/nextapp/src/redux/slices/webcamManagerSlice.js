import {createSlice} from "@reduxjs/toolkit";

const webcamManagerSlice = createSlice({
    name: 'webcamManager',
    initialState: {
        webcamDevices: [],
        frame: null,
        isWebcamOn: false,
        isRecording: false,
        canSendRecords: false
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
        setWebcamState: (state, action) => {
            state.isWebcamOn = action.payload;
        },
        setRecordState: (state, action) => {
            if (state.isRecording) state.canSendRecords = true;
            state.isRecording = action.payload;
        },
        reorderWebcamDevices: (state, action) => {
            const selectedDevice = action.payload;
            state.webcamDevices = [selectedDevice, ...state.webcamDevices.filter(device => device !== selectedDevice)];
        },
        sendRecords: (state, action) => {
            state.canSendRecords = false;
        }
    }
});

export const {
    setWebcamDevices,
    setWebcamFrame,
    reorderWebcamDevices,
    setWebcamState,
    setRecordState,
    sendRecords,
}  = webcamManagerSlice.actions;


export const webcamManagerReducer = webcamManagerSlice.reducer;