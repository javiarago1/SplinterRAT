import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Paper, Tabs, Tab as MuiTab } from '@mui/material';
import FileManager from "../Funcionalities/FileManager/FileManager";
import { Button } from '@mui/material';


function ClientManager({ client, onBack }) {
    const dispatch = useDispatch();

    const [currentTab, setCurrentTab] = useState(0);
    const [clientState, setClientState] = useState({
        // Aquí puedes mantener cualquier otro estado específico del cliente que necesites
    });

    useEffect(() => {
        // Reinicia el estado cuando cambies de cliente
        setClientState({
            // Reinicia el estado específico del cliente a sus valores predeterminados
        });
        setCurrentTab(0);
    }, [client]);

    const handleChange = (event, newValue) => {
        setCurrentTab(newValue);
    };

    return (
        <div>
            <Button onClick={onBack} >Volver a la tabla</Button>
            <Paper>
                <Tabs value={currentTab} onChange={handleChange}>
                    <MuiTab label="Information" />
                    <MuiTab label="File manager" />
                    <MuiTab label="Contact" />
                </Tabs>

                {currentTab === 0 && (
                    <div>
                        {client.systemInformation.UUID} - {client.systemInformation.USER_NAME}
                    </div>
                )}
                {currentTab === 1 && <FileManager />}
                {currentTab === 2 && <div>Contacto de {client.name}</div>}
            </Paper>
        </div>
    );
}

export default ClientManager;
