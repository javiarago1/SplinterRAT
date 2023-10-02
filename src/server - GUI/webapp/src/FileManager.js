import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { requestDisks, reorderDisks } from './clientSlice';
import { Autocomplete, TextField } from '@mui/material';

function FileManager( {currentTab} ) {
    const dispatch = useDispatch();
    const selectedClient = useSelector(state => state.client.selectedClient);
    const disks = useSelector(state => state.client.disks);


    useEffect(() => {
        if (disks.length === 0 && selectedClient) {
            dispatch(requestDisks(selectedClient.systemInformation.UUID));
        }
    }, [selectedClient, dispatch, disks]);

    const handleDiskChange = (event, newValue) => {
        if (newValue) {
            dispatch(reorderDisks(newValue));
        }
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
                        <TextField {...params} label="Select Disk" variant="outlined" />
                    )}
                />
            )}
        </div>
    );
}

export default FileManager;
