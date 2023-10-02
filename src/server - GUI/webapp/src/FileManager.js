// FileManager.js

import React, { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { requestDisks } from './clientSlice';
import { Autocomplete, TextField } from '@mui/material';

function FileManager( {currentTab} ) {
    const dispatch = useDispatch();
    const selectedClient = useSelector(state => state.client.selectedClient);
    const disksForSelectedClient = useSelector(state => state.client.disks[selectedClient.UUID]);

    const [disksLoaded, setDisksLoaded] = React.useState(false);


    // Cuando se monta el componente, solicita los discos del cliente seleccionado
    useEffect(() => {
        if (!disksLoaded && selectedClient) {
            dispatch(requestDisks(selectedClient.systemInformation.UUID));
            setDisksLoaded(true);
        }
    }, [selectedClient, dispatch, disksLoaded]);


    console.log(selectedClient)

    return (
        <div>
            {disksForSelectedClient && (
                <Autocomplete
                    options={disksForSelectedClient}
                    defaultValue={disksForSelectedClient[0]} // Selecciona el primer disco por defecto
                    freeSolo={false} // Evita la entrada de texto libre
                    disableClearable // Evita que el usuario borre el valor seleccionado
                    renderInput={(params) => (
                        <TextField {...params} label="Select Disk" variant="outlined" />
                    )}
                />
            )}
        </div>
    );

}

export default FileManager;
