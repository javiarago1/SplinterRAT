import React, {useEffect, useCallback, useState} from 'react';
import {
    Button,
    Select,
    MenuItem,
    Grid,
    CircularProgress,
    DialogTitle,
    Dialog,
    DialogActions,
    DialogContent, DialogContentText
} from '@mui/material';
import {useDispatch, useSelector} from 'react-redux';
import {
    START_DOWNLOAD,
    START_WEBCAM,
    WEBCAM_DEVICES,
    STOP_WEBCAM,
    START_RECORDING_WEBCAM, STOP_RECORDING_WEBCAM, SEND_WEBCAM_RECORDS
} from "@redux/actions/webcamManagerActions";
import WebcamFrame from "@components/Funcionalities/WebcamManager/WebcamFrame";
import {
    reorderWebcamDevices,
    setRecordState,
    setWebcamState,
    sendRecords,
    setSendState
} from "@redux/slices/webcamManagerSlice";

const WebcamViewer = () => {
    const dispatch = useDispatch();
    const [open, setOpen] = useState(false)
    const {isWebcamOn, isRecording, webcamDevices, canSendRecords} = useSelector(state => state.webcamManager);

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
        if (isWebcamOn && (canSendRecords || isRecording)) {
            setOpen(true)
            console.log("called!!!!!")
            console.log(isWebcamOn + "|"+canSendRecords+"|"+isRecording)
        } else {
            dispatch(!isWebcamOn ? {
                type: START_WEBCAM,
                payload: {selected_device: webcamDevices[0]}
            } : {type: STOP_WEBCAM});
            dispatch(setWebcamState(!isWebcamOn));
        }
    }

    const toggleRecording = () => {
        dispatch({type: !isRecording ? START_RECORDING_WEBCAM : STOP_RECORDING_WEBCAM});
        dispatch(setSendState(isRecording));
        dispatch(setRecordState(!isRecording));
    }

    const toggleSendRecords = () => {
        dispatch(sendRecords())
        dispatch(setRecordState(false));
        dispatch(setRecordState(!isRecording));
        dispatch({type: SEND_WEBCAM_RECORDS});
    }

    // Si no hay dispositivos, mostrar un indicador de carga
    if (!webcamDevices || webcamDevices.length === 0) {
        return <CircularProgress/>;
    }

    const handleAccept = () => {
        toggleSendRecords();
        toggleStart();
        setOpen(false)
    };

    const handleDeny = () => {
        toggleStart();
        setOpen(false)
    };

    return (
        <div>
            <Dialog
                open={open}
                onClose={handleDeny}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="alert-dialog-title">{"Warning: pending recordings to save?"}</DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        You have pending recordings to save. Do you want to save the recordings?
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleDeny} color="primary">
                        Discard
                    </Button>
                    <Button onClick={handleAccept} color="primary" autoFocus>
                        Download
                    </Button>
                </DialogActions>
            </Dialog>
            <WebcamFrame/>
            <Grid container spacing={2}>
                <Grid item>
                    <Button onClick={toggleStart}>
                        {isWebcamOn ? "Stop Webcam" : "Start Webcam"}
                    </Button>
                </Grid>
                <Grid item>
                    <Select disabled={isWebcamOn}
                            value={webcamDevices[0]}
                            onChange={(e) => handleChangeDevice(e)}
                    >
                        {webcamDevices.map((device) => (
                            <MenuItem key={device} value={device}>{device}</MenuItem>
                        ))}
                    </Select>
                </Grid>
                <Grid item>
                    <Button disabled={!isWebcamOn} onClick={toggleRecording}>
                        {isRecording ? "Stop Recording" : "Start Recording"}
                    </Button>
                </Grid>
                <Grid item>
                    <Button disabled={!canSendRecords} onClick={toggleSendRecords}>
                        Download records
                    </Button>
                </Grid>
            </Grid>
        </div>
    );
};

export default WebcamViewer;
