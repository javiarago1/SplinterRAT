// ClientManager.js

import React from 'react';
import { useSelector } from 'react-redux';
import { Paper, Tabs, Tab as MuiTab } from '@mui/material';
import FileManager from "./FileManager";

function ClientManager() {
    const selectedClient = useSelector(state => state.client.selectedClient);

    const [currentTab, setCurrentTab] = React.useState(0);

    const handleChange = (event, newValue) => {
        setCurrentTab(newValue);
    };

    return (
        <Paper>
            <Tabs value={currentTab} onChange={handleChange}>
                <MuiTab label="Information" />
                <MuiTab label="File manager!!!" />
                <MuiTab label="Contact" />
            </Tabs>

            <div style={{ display: currentTab === 0 ? 'block' : 'none' }}>
                {selectedClient.systemInformation.UUID} - {selectedClient.systemInformation.USER_NAME}
            </div>
            <div style={{ display: currentTab === 1 ? 'block' : 'none' }}>
                <FileManager />
            </div>
            <div style={{ display: currentTab === 2 ? 'block' : 'none' }}>
                Contacto de {selectedClient.name}
            </div>
        </Paper>
    );
}

export default ClientManager;
