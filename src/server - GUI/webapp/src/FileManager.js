import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { popDirectory, reorderDisks } from './clientSlice';
import { Autocomplete, TextField, Button, Grid } from '@mui/material';
import { REQUEST_DISKS, REQUEST_DIRECTORY } from "./fileManagerActions";
import FileTable from "./fileTable";

function FileManager({ currentTab }) {
    const dispatch = useDispatch();
    const selectedClient = useSelector(state => state.client.selectedClient);
    const disks = useSelector(state => state.client.disks);
    const directoryStack = useSelector(state => state.client.directoryStack);

    useEffect(() => {
        if (disks.length === 0 && selectedClient) {
            dispatch({
                type: REQUEST_DISKS,
                payload: {client_id: selectedClient.systemInformation.UUID, sendDirectory: true}
            });
        }
    }, [selectedClient, dispatch, disks]);

    const handleDiskChange = (event, newValue) => {
        if (newValue) {
            dispatch(reorderDisks(newValue));
            dispatch({
                type: REQUEST_DIRECTORY,
                payload: {client_id: selectedClient.systemInformation.UUID, path: newValue}
            });
        }
    };

    const handleGoBack = () => {
        const path = directoryStack[directoryStack.length - 2].requested_directory;
        dispatch(popDirectory());
        dispatch({type: REQUEST_DIRECTORY, payload: {client_id: selectedClient.systemInformation.UUID, path: path}});
    };

    const currentDirectory = directoryStack.length > 0 ? directoryStack[directoryStack.length - 1].requested_directory : '';

    return (
        <div>
            <Grid container spacing={2} alignItems="center">
                <Grid item xs={6} sm={7} md={8}>
                    <TextField
                        label="Current Directory"
                        value={currentDirectory}
                        variant="outlined"
                        fullWidth
                        InputProps={{
                            readOnly: true,
                        }}
                    />
                </Grid>
                <Grid item xs={1}>
                    <Button variant="contained" color="primary" onClick={() => dispatch({ type: REQUEST_DIRECTORY, payload: { client_id: selectedClient.systemInformation.UUID, path: currentDirectory } })}>
                        Refresh Directory
                    </Button>
                </Grid>
                <Grid item xs={3} sm={2} md={2}>
                    {disks && (
                        <Autocomplete
                            options={disks}
                            value={disks[0] || null}
                            onChange={handleDiskChange}
                            freeSolo={false}
                            disableClearable
                            renderInput={(params) => (
                                <TextField {...params} label="Select Disk" variant="outlined" fullWidth />
                            )}
                        />
                    )}
                </Grid>
                <Grid item xs={2} sm={2} md={1}>
                    <Button variant="contained" color="primary" onClick={() => dispatch({ type: REQUEST_DISKS, payload: { client_id: selectedClient.systemInformation.UUID, sendDirectory: true } })}>
                        Refresh Disks
                    </Button>
                </Grid>
            </Grid>
            <Grid container spacing={2} alignItems="center" style={{ marginTop: '10px' }}>
                <Grid item xs={1}>
                    <Button variant="contained" color="primary" onClick={handleGoBack} disabled={directoryStack.length < 2}>
                        Go Back
                    </Button>
                </Grid>
            </Grid>
            <FileTable />
        </div>
    );
}

export default FileManager;
