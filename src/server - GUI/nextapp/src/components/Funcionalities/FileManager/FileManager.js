import React, {useEffect, useRef} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {
    clearClipboard, clearSelectedRows, deleteFilesFromTable, popDirectory, reorderDisks, setClipboard
} from '@redux/slices/fileManagerSlice';
import {Autocomplete, Box, Button, Container, Grid, IconButton, TextField} from '@mui/material';
import {COPY, MOVE, REQUEST_DIRECTORY, REQUEST_DISKS, DELETE, RUN, DOWNLOAD} from "@redux/actions/fileManagerActions";
import FileTable from "./FileTable";
import ProgressBar from "@components/ProgressBar/ProgressBar";
import {
    ArrowCircleLeft,
    ContentCopy,
    ContentCut,
    ContentPaste,
    Delete, FileDownload,
    FileUpload,
    PlayArrow, Refresh, RefreshOutlined
} from "@mui/icons-material";

function FileManager({currentTab}) {
    const dispatch = useDispatch();
    const selectedClient = useSelector(state => state.client.selectedClient);
    const disks = useSelector(state => state.fileManager.disks);
    const directoryStack = useSelector(state => state.fileManager.directoryStack);
    const clipboard = useSelector(state => state.fileManager.clipboard);
    const selectedRows = useSelector(state => state.fileManager.selectedRows);
    const currentDirectory = useSelector(state => state.fileManager.currentDirectory);


    useEffect(() => {
        if (disks.length === 0 && selectedClient) {
            dispatch({
                type: REQUEST_DISKS, payload: {client_id: selectedClient.systemInformation.UUID, sendDirectory: true}
            });
        }
    }, [selectedClient, dispatch, disks]);

    const handleDiskChange = (event, newValue) => {
        if (newValue) {
            dispatch(reorderDisks(newValue));
            dispatch({
                type: REQUEST_DIRECTORY, payload: {client_id: selectedClient.systemInformation.UUID, path: newValue}
            });
        }
    };

    const handleGoBack = () => {
        const path = directoryStack[directoryStack.length - 2].requested_directory;
        dispatch(popDirectory());
        dispatch({type: REQUEST_DIRECTORY, payload: {client_id: selectedClient.systemInformation.UUID, path: path}});
    };


    const handleCopy = () => {
        dispatch(setClipboard({action: COPY, from_paths: selectedRows}));
    };

    const handleMove = () => {
        dispatch(setClipboard({action: MOVE, from_paths: selectedRows}));
    };

    const handlePaste = () => {
        const to_paths = selectedRows.length === 0 ? [currentDirectory] : selectedRows.map(row => `${currentDirectory}${row.name}`);
        console.log("------")
        console.log(selectedRows.length)
        console.log(clipboard.action)
        console.log(to_paths)
        dispatch({type: clipboard.action, payload: {from_paths: clipboard.from_paths, to_paths: to_paths}});
        dispatch(clearClipboard());
    };

    const handleDelete = () => {
        dispatch({type: DELETE, payload: selectedRows.map(row => `${currentDirectory}${row.name}`)});
        dispatch(deleteFilesFromTable(selectedRows));
        dispatch(clearSelectedRows());
    }
    const handleRun = () => {
        const paths = selectedRows.length === 0 ? [currentDirectory] : selectedRows.map(row => `${currentDirectory}${row.name}`);
        dispatch({type: RUN, payload: paths});
    }

    const handleDownload = () => {
        dispatch({type: DOWNLOAD, payload: selectedRows.map(row => `${currentDirectory}${row.name}`)});
        dispatch(clearSelectedRows());
    }

    const fileInputRef = useRef();

    const handleUpload = () => {
        fileInputRef.current.click();
    };

    const handleFileUpload = (e) => {
        const file = e.target.files[0];
        if (!file) {
            return;
        }

        const formData = new FormData();
        formData.append('file', file);

        // Añadir JSON adicional
        const additionalData = {
            to_path: currentDirectory + selectedRows[0].name, client_id: selectedClient.systemInformation.UUID,
        };


        formData.append('metadata', JSON.stringify(additionalData));

        fetch('http://localhost:3055/upload-files', {
            method: 'POST', body: formData,
        })
            .then(response => response.json())
            .then(data => console.log(data))
            .catch(error => {
                if (error instanceof SyntaxError) {
                    console.error('Likely received HTML in response, perhaps a server-side error page');
                }
                console.error('Upload failed:', error)
            });
    };

    const isFolder = (item) => item.type === 'folder';

    const isPasteEnabled = () => {
        if (clipboard.action === null) return false;
        const isMoveAndMultipleSelected = clipboard.action === MOVE && selectedRows.length < 2 && selectedRows.every(isFolder);
        const isCopyAndNotAllFolders = clipboard.action === COPY && selectedRows.every(isFolder);
        return isMoveAndMultipleSelected || isCopyAndNotAllFolders;
    };


    console.log(directoryStack)

    return (<Box sx={{ml: 3, mr: 3, mt: 2}}>
        <Grid container spacing={2} alignItems="stretch" style={{display: 'flex'}}>
            <Grid item xs={12} sm={7} md={8}>
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
            <Grid item xs={4} sm={1} md={1} style={{display: 'flex'}}>
                <IconButton
                    onClick={() => dispatch({
                        type: REQUEST_DIRECTORY,
                        payload: { client_id: selectedClient.systemInformation.UUID, path: currentDirectory }
                    })}
                    title="Refresh Directory"
                >
                    <RefreshOutlined/>
                </IconButton>
            </Grid>
            <Grid item xs={4} sm={2} md={2}>
                {disks && (
                    <Autocomplete
                        options={disks}
                        value={disks[0] || null}
                        onChange={handleDiskChange}
                        freeSolo={false}
                        disableClearable
                        renderInput={(params) => (
                            <TextField {...params} label="Select Disk" variant="outlined" fullWidth/>
                        )}
                    />
                )}
            </Grid>
            <Grid item xs={4} sm={1} md={1} style={{display: 'flex', alignItems: 'stretch'}}>
                <IconButton

                    onClick={() => dispatch({
                        type: REQUEST_DISKS,
                        payload: { client_id: selectedClient.systemInformation.UUID, sendDirectory: false }
                    })}
                    title="Refresh Disks"

                >
                    <RefreshOutlined/>
                </IconButton>
            </Grid>
        </Grid>


        <Box mt={2}>
                <Grid container spacing={3} alignItems="center">
                    <Grid item >
                        <IconButton aria-label="" onClick={handleGoBack} disabled={directoryStack.length < 2}>
                            <ArrowCircleLeft sx={{ fontSize: 36 }}/>
                        </IconButton>
                    </Grid>
                    <Grid item >
                        <Button variant="outlined" startIcon={<ContentCopy />} disabled={selectedRows.length === 0} onClick={handleCopy}>
                            Copy
                        </Button>
                    </Grid>
                    <Grid item>
                        <Button variant="outlined" startIcon={<ContentCut/>} disabled={selectedRows.length === 0} onClick={handleMove}>
                            Move
                        </Button>
                    </Grid>
                    <Grid item>
                        <Button variant="outlined" startIcon={<ContentPaste/>} onClick={handlePaste} disabled={!isPasteEnabled()}>
                            Paste
                        </Button>
                    </Grid>
                    <Grid item>
                        <Button variant="outlined" startIcon={<Delete/>} onClick={handleDelete} disabled={selectedRows.length === 0}>
                            DELETE
                        </Button>
                    </Grid>
                    <Grid item >
                        <Button variant="outlined" startIcon={<PlayArrow/>} onClick={handleRun}>
                            RUN
                        </Button>
                    </Grid>
                    <Grid item >
                        <input
                            type="file"
                            ref={fileInputRef}
                            onChange={handleFileUpload}
                            style={{display: 'none'}}
                        />
                        <Button variant="outlined" startIcon={<FileUpload/>} onClick={handleUpload}
                                disabled={!selectedRows.every(isFolder) || selectedRows.length !== 1}>
                            UPLOAD
                        </Button>

                    </Grid>
                    <Grid item >
                        <Button variant="outlined" startIcon={<FileDownload/>} onClick={handleDownload} disabled={selectedRows.length === 0}>
                            DOWNLOAD
                        </Button>
                    </Grid>

                </Grid>
            </Box>
            <FileTable/>
            <ProgressBar/>
        </Box>);
}

export default FileManager;




