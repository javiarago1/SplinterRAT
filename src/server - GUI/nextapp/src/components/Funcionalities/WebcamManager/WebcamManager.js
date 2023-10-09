import React, { useEffect, useCallback } from 'react';
import { Button, Select, MenuItem, Grid } from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import { setDevices } from '@redux/slices/webcamManagerSlice';
import {START_DOWNLOAD, START_WEBCAM} from "@redux/actions/webcamManagerActions";

const WebcamViewer = () => {
    const dispatch = useDispatch();
    const { isWebcamOn, isRecording, devices } = useSelector(state => state.webcamManager);

    const handleChangeDevice = useCallback(
        (event) => {
            //dispatch(setDevice(event.target.value));
        },[dispatch]
    );



    const toggleStart = () => {
        dispatch({type: START_WEBCAM,  payload: {selected_device: 0, }});
    }

    const toggleRecording = () => {
        dispatch({type: START_DOWNLOAD});
    }

    return (
        <div>
            {isWebcamOn}
            <Grid container spacing={2}>
                <Grid item>
                    <Button onClick={() => toggleStart()}>
                        {isWebcamOn ? "Stop Webcam" : "Start Webcam"}
                    </Button>
                </Grid>
                <Grid item>
                    <Select
                        value={devices[0]}
                        onChange={(e) => dispatch(handleChangeDevice(e))}
                    >
                        {devices.map((device) => (
                            <MenuItem key={"a"} value={device.deviceId}>{device.label}</MenuItem>
                        ))}
                    </Select>
                </Grid>
                <Grid item>
                    <Button onClick={() => toggleRecording()}>
                        {isRecording ? "Stop Recording" : "Start Recording"}
                    </Button>
                </Grid>
                <Grid item>
                    {/* ... Additional controls as per your requirement */}
                </Grid>
            </Grid>
        </div>
    );
};

export default WebcamViewer;
