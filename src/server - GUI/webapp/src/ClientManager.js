import React from 'react';
import { useSelector } from 'react-redux'; // importa useSelector
import { Paper, Tabs, Tab as MuiTab } from '@mui/material';

function ClientManager() {
    const selectedClient = useSelector(state => state.client.selectedClient); // accede al cliente seleccionado

    const [currentTab, setCurrentTab] = React.useState(0);

    const handleChange = (event, newValue) => {
        setCurrentTab(newValue);
    };

    return (
        <Paper>
            <Tabs value={currentTab} onChange={handleChange}>
                <MuiTab label="Details" />
                <MuiTab label="Purchase History" />
                <MuiTab label="Contact" />
            </Tabs>
            {currentTab === 0 && <div>{selectedClient.name} - {selectedClient.email}</div>}
            {currentTab === 1 && <div>Historial de compras de {selectedClient.name}</div>}
            {currentTab === 2 && <div>Contacto de {selectedClient.name}</div>}
        </Paper>
    );
}

export default ClientManager;
