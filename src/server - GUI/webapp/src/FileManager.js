import React, {useEffect} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {popDirectory, reorderDisks} from './clientSlice';
import {Autocomplete, TextField} from '@mui/material';
import {REQUEST_DISKS, REQUEST_DIRECTORY} from "./fileManagerActions";
import FileTable from "./fileTable";
import {Button} from "@mui/material";

function FileManager({currentTab}) {
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
        console.log("To go "+path)
        dispatch(popDirectory());
        dispatch({type: REQUEST_DIRECTORY, payload: {client_id: selectedClient.systemInformation.UUID, path: path}});
    };

    return (
        <div>

            {disks && (
                <Autocomplete
                    options={disks}
                    value={disks[0] || null}
                    onChange={handleDiskChange}
                    freeSolo={false}
                    disableClearable
                    renderInput={(params) => (
                        <TextField {...params} label="Select Disk" variant="outlined"/>
                    )}
                />
            )}
            <Button onClick={handleGoBack} disabled={directoryStack.length < 2}>Go Back</Button>
            <FileTable/>
        </div>
    );
}

export default FileManager;
