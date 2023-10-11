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
    DialogContent, DialogContentText, Box, Divider
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
import {FiberManualRecord, PlayArrow, Save, Stop} from "@mui/icons-material";

const WebcamViewer = () => {
    const dispatch = useDispatch();
    const [openFirstDialog, setOpenFirstDialog] = useState(false)
    const [openSecondDialog, setOpenSecondDialog] = useState(false)
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
        if (isWebcamOn && isRecording) {
            setOpenFirstDialog(true)
        } else if (isWebcamOn && canSendRecords) {
            setOpenSecondDialog(true);
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
        dispatch(setRecordState(!isRecording));
    }

    const toggleSendRecords = () => {
        dispatch(sendRecords())
        dispatch({type: SEND_WEBCAM_RECORDS});
    }

    if (!webcamDevices || webcamDevices.length === 0) {
        return <CircularProgress/>;
    }

    const firstDialogAccept = () => {
        toggleRecording();
        setOpenFirstDialog(false)
    }

    const secondDialogAccept = () => {
        toggleSendRecords();
        setOpenSecondDialog(false);
    }


    return (
        <div>
            <Dialog
                open={openFirstDialog}
                onClose={() => setOpenFirstDialog(false)}
                aria-labelledby="is-currently-recording"
                aria-describedby="alert-dialog-description"
            >
                <DialogTitle id="is-currently-recording">{"Warning: you are currently recording"}</DialogTitle>
                <DialogContent>
                    <DialogContentText id="alert-dialog-description">
                        You are currently recording, finish recording in order to stop streaming the webcam.
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setOpenFirstDialog(false)} color="primary">
                        Cancel
                    </Button>
                    <Button onClick={firstDialogAccept} color="primary" autoFocus>
                        Stop recording
                    </Button>
                </DialogActions>
            </Dialog>
            <Dialog
                open={openSecondDialog}
                onClose={() => setOpenSecondDialog(false)}
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
                    <Button onClick={toggleStart} color="primary">
                        Discard
                    </Button>
                    <Button onClick={secondDialogAccept} color="primary" autoFocus>
                        Save recordings
                    </Button>
                </DialogActions>
            </Dialog>
            <Box margin={2} padding={2} pb={3} >

                <Grid container spacing={3} alignItems="flex-start">
                    {/* Webcam Frame */}
                    <Grid item>
                        <WebcamFrame/>
                    </Grid>

                    {/* Controls */}
                    <Grid item xs>
                        <Grid container direction="column" spacing={2} alignItems="flex-start">
                            <Grid item>
                                <Button variant="outlined" startIcon={!isWebcamOn ? <PlayArrow/> : <Stop/>}
                                        onClick={toggleStart}>
                                    {isWebcamOn ? "Stop" : "Start"}
                                </Button>
                            </Grid>
                            <Grid item>
                                <Select
                                    disabled={isWebcamOn}
                                    value={webcamDevices[0]}
                                    onChange={(e) => handleChangeDevice(e)}
                                >
                                    {webcamDevices.map((device) => (
                                        <MenuItem key={device} value={device}>{device}</MenuItem>
                                    ))}
                                </Select>
                            </Grid>
                            <Grid item>
                                <Button variant="outlined" startIcon={!isRecording ? <FiberManualRecord/> : <Stop/>}
                                        disabled={!isWebcamOn} onClick={toggleRecording}>
                                    {isRecording ? "Stop record" : "Record"}
                                </Button>
                            </Grid>
                            <Grid item>
                                <Button variant="outlined" startIcon={<Save/>} disabled={!canSendRecords}
                                        onClick={toggleSendRecords}>
                                    Download records
                                </Button>
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
            </Box>
        </div>);

};

export default WebcamViewer;
