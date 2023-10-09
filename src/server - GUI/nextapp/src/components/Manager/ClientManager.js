import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Paper, Tabs, Tab as MuiTab } from '@mui/material';
import FileManager from "../Funcionalities/FileManager/FileManager";
import { Button } from '@mui/material';
import WebcamManager from "@components/Funcionalities/WebcamManager/WebcamManager";


function ClientManager({ client, onBack }) {
    const dispatch = useDispatch();

    const [currentTab, setCurrentTab] = useState(0);

    useEffect(() => {
        setCurrentTab(0);
    }, [client]);

    const handleChange = (event, newValue) => {
        setCurrentTab(newValue);
    };

    return (
        <div style={{height: '100%'}}>
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
                {currentTab === 2 && <WebcamManager />}
            </Paper>
        </div>
    );
}

export default ClientManager;
