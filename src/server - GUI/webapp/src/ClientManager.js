import React from 'react';
import { useSelector } from 'react-redux'; // importa useSelector
import { Paper, Tabs, Tab as MuiTab } from '@mui/material';
import FileManager from "./FileManager";

function ClientManager() {
    const selectedClient = useSelector(state => state.client.selectedClient); // accede al cliente seleccionado

    const [currentTab, setCurrentTab] = React.useState(0);

    const handleChange = (event, newValue) => {
        setCurrentTab(newValue);
    };

    console.log(selectedClient);

    return (
        <Paper>
            <Tabs value={currentTab} onChange={handleChange}>
                <MuiTab label="Information" />
                <MuiTab label="File manager!!!" />
                <MuiTab label="Contact" />
            </Tabs>

            {currentTab === 0 && <div>{selectedClient.systemInformation.UUID} - {selectedClient.systemInformation.USER_NAME}</div>}
            {currentTab === 1 && <FileManager />}
            {currentTab === 2 && <div>Contacto de {selectedClient.name}</div>}
        </Paper>
    );
}

export default ClientManager;
