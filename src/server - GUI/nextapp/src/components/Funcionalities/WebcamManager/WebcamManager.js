import React, { useEffect, useCallback } from 'react';
import { Button, Select, MenuItem, Grid, CircularProgress } from '@mui/material';
import { useDispatch, useSelector } from 'react-redux';
import { START_DOWNLOAD, START_WEBCAM, WEBCAM_DEVICES } from "@redux/actions/webcamManagerActions";
import WebcamFrame from "@components/Funcionalities/WebcamManager/WebcamFrame";
import {reorderWebcamDevices} from "@redux/slices/webcamManagerSlice";

const WebcamViewer = () => {
    const dispatch = useDispatch();
    const { isWebcamOn, isRecording, webcamDevices } = useSelector(state => state.webcamManager);

    useEffect(() => {
        if (!webcamDevices || webcamDevices.length === 0) {
            dispatch({type: WEBCAM_DEVICES});
        }
    }, [dispatch, webcamDevices]);

    const handleChangeDevice = useCallback(
        (event) => {
            const selectedDevice = event.target.value;
            dispatch(reorderWebcamDevices(selectedDevice));
        },
        [dispatch]
    );

    const toggleStart = () => {
        dispatch({type: START_WEBCAM, payload: {selected_device: webcamDevices[0]}});
    }

    const toggleRecording = () => {
        dispatch({type: START_DOWNLOAD});
    }

    // Si no hay dispositivos, mostrar un indicador de carga
    if (!webcamDevices || webcamDevices.length === 0) {
        return <CircularProgress />;
    }

    return (
        <div>
            <WebcamFrame />
            <Grid container spacing={2}>
                <Grid item>
                    <Button onClick={toggleStart}>
                        {isWebcamOn ? "Stop Webcam" : "Start Webcam"}
                    </Button>
                </Grid>
                <Grid item>
                    <Select
                        value={webcamDevices[0]}
                        onChange={(e) => handleChangeDevice(e)}
                    >
                        {webcamDevices.map((device) => (
                            <MenuItem key={device} value={device}>{device}</MenuItem>
                        ))}
                    </Select>
                </Grid>
                <Grid item>
                    <Button onClick={toggleRecording}>
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
