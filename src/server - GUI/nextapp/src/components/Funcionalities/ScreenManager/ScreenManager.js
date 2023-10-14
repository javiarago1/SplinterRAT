import React, {useEffect, useState} from 'react';
import {
    Button,
    Select,
    MenuItem,
    Grid,
    Box,
    Checkbox,
    FormControlLabel, CircularProgress
} from '@mui/material';
import {PlayArrow, Stop} from "@mui/icons-material";
import ScreenFrame from "@components/Funcionalities/ScreenManager/ScreenFrame";
import {useDispatch, useSelector} from "react-redux";
import {MONITORS, START_SCREEN_STREAMING, STOP_SCREEN_STREAMING} from "@redux/actions/screenManagerActions";
import {reorderScreens, setScreenState, setCanControl} from "@redux/slices/screenManagerSlice";

const ScreenManager = () => {
    const dispatch = useDispatch();
    const {isScreenOn, screens, originalDimensions , canControl} = useSelector((state) => state.screenManager);

    useEffect(() => {
        if (!screens || screens.length === 0) {
            dispatch({type: MONITORS});
        }
    }, [dispatch, screens]);

    if (!screens || screens.length === 0) {
        return <CircularProgress/>;
    }


    const toggleStreaming = () => {
        dispatch(isScreenOn ? {type: STOP_SCREEN_STREAMING} : {
            type: START_SCREEN_STREAMING,
            payload: {monitor_id: screens[0]}
        });
        dispatch(setScreenState(!isScreenOn));
    };

    const handleScreenChange = (event) => {
        dispatch(reorderScreens(event.target.value));
    };
    const handleControlToggle = (event) => {
        dispatch(setCanControl(event.target.checked));
    };

    return (
        <>
        <Box sx={{height: '65vh', overflow:'auto'}}  ml={3} mt={2} mb={1}>
            <Grid container spacing={3} alignItems="flex-start">
                <Grid item>
                    <ScreenFrame/>
                </Grid>
                {/* Controls */}
                <Grid item xs>
                    <Grid container direction="column" spacing={2} alignItems="flex-start">
                        <Grid item>
                            <Button variant="outlined" startIcon={!isScreenOn ? <PlayArrow/> : <Stop/>}
                                    onClick={toggleStreaming}>
                                {isScreenOn ? "Stop streaming" : "Start streaming"}
                            </Button>
                        </Grid>

                        <Grid item>
                            <Select
                                disabled={isScreenOn}
                                value={screens[0]}
                                onChange={handleScreenChange}
                            >
                                {screens.map((screen) => (
                                    <MenuItem key={screen} value={screen}>{screen}</MenuItem>
                                ))}
                            </Select>
                        </Grid>

                        <Grid item>
                            <FormControlLabel
                                control={
                                    <Checkbox
                                        variant=""
                                        checked={canControl}
                                        onChange={handleControlToggle}
                                        name="controlToggle"
                                        color="primary"
                                    />
                                }
                                label="Control"
                            />
                        </Grid>
                    </Grid>
                </Grid>
            </Grid>
        </Box>
        </>
    );
};

export default ScreenManager;
