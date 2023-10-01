import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { Paper, Tabs, Tab as MuiTab } from '@mui/material';
import ClientManager from './ClientManager';
import ClientTable from "./ClientTable";

function MainTabs() {
    const clients = useSelector(state => state.client.tabs);
    const [currentTab, setCurrentTab] = useState(0);

    const handleChange = (event, newValue) => {
        setCurrentTab(newValue);
    };

    console.log(clients);
    console.log(currentTab)

    return (
        <div>
            <Paper>
                <Tabs value={currentTab} onChange={handleChange}>
                    {clients.map(client => (
                        <MuiTab key={client.uuid} label={client.name} />
                    ))}
                </Tabs>
            </Paper>
            {currentTab === 0 ? <ClientTable clients={clients} setCurrentTab={setCurrentTab} /> : <ClientManager client={clients[currentTab]} />}
        </div>
    );
}


export default MainTabs;
