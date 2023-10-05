import React, {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {clearClipboard, popDirectory, reorderDisks, setClipboard} from './clientSlice';
import {Autocomplete, Button, Grid, TextField} from '@mui/material';
import {COPY, MOVE, REQUEST_DIRECTORY, REQUEST_DISKS} from "./fileManagerActions";
import FileTable from "./fileTable";

function FileManager({ currentTab }) {
    const dispatch = useDispatch();
    const selectedClient = useSelector(state => state.client.selectedClient);
    const disks = useSelector(state => state.fileManager.disks);
    const directoryStack = useSelector(state => state.fileManager.directoryStack);
    const clipboard  = useSelector(state => state.fileManager.clipboard);
    const selectedRows = useSelector(state => state.fileManager.selectedRows);
    const currentDirectory = useSelector(state => state.fileManager.currentDirectory);


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


    const handleCopy = () => {
        dispatch(setClipboard({ action: COPY, from_paths: selectedRows }));
    };

    const handleMove = () => {
        dispatch(setClipboard({ action: MOVE, from_paths: selectedRows }));
    };

    const handlePaste = () => {
        const to_paths = selectedRows.length === 0 ? [currentDirectory] : selectedRows.map(row => `${currentDirectory}\\${row.name}`);
        console.log("------")
        console.log(clipboard.action)
        console.log(to_paths)
        dispatch({ type: clipboard.action, payload: { from_paths: clipboard.from_paths, to_paths: to_paths}});
        dispatch(clearClipboard());
    };

    const isFolder = (item) => item.type === 'folder';

    const isPasteEnabled = () => {
        if (clipboard.action === null) return false;
        const isMoveAndMultipleSelected = clipboard.action === MOVE && selectedRows.length < 2 && selectedRows.every(isFolder);
        const isCopyAndNotAllFolders = clipboard.action === COPY && selectedRows.every(isFolder);
        return isMoveAndMultipleSelected || isCopyAndNotAllFolders;
    };

    console.log(directoryStack)

    return (
        <div>
            <Grid container spacing={2} alignItems="center">
                <Grid item xs={6} sm={7} md={8}>
                    <TextField
                        label="Current Directory"
                        value={currentDirectory === null ? ' ' : currentDirectory}
                        variant="outlined"
                        fullWidth
                        InputProps={{
                            readOnly: true,
                        }}
                    />
                </Grid>
                <Grid item xs={1}>
                    <Button  onClick={() => dispatch({ type: REQUEST_DIRECTORY, payload: { client_id: selectedClient.systemInformation.UUID, path: currentDirectory } })}>
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
                    <Button  onClick={() => dispatch({ type: REQUEST_DISKS, payload: { client_id: selectedClient.systemInformation.UUID, sendDirectory: false } })}>
                        Refresh Disks
                    </Button>
                </Grid>
            </Grid>
            <Grid container spacing={2} alignItems="center" style={{ marginTop: '10px' }}>
                <Grid container spacing={2} alignItems="center" style={{ marginTop: '10px' }}>
                    <Grid item xs={1}>
                        <Button onClick={handleGoBack} disabled={directoryStack.length < 2}>
                            Go Back
                        </Button>
                    </Grid>
                    <Grid item xs={1}>
                        <Button disabled={selectedRows.length === 0} onClick={handleCopy}>
                            Copy
                        </Button>
                    </Grid>
                    <Grid item xs={1}>
                        <Button disabled={selectedRows.length === 0} onClick={handleMove}>
                            Move
                        </Button>
                    </Grid>
                    <Grid item xs={1}>
                        <Button onClick={handlePaste} disabled={!isPasteEnabled()}>
                            Paste
                        </Button>
                    </Grid>
                </Grid>
            </Grid>
            <FileTable />
        </div>
    );
}

export default FileManager;
