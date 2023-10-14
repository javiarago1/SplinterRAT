import React, {useEffect, useState} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {Paper, Tabs, Tab as MuiTab, IconButton} from '@mui/material';
import FileManager from "../Funcionalities/FileManager/FileManager";
import {Button} from '@mui/material';
import WebcamManager from "@components/Funcionalities/WebcamManager/WebcamManager";
import {ArrowBack, Toc} from "@mui/icons-material";
import ScreenManager from "@components/Funcionalities/ScreenManager/ScreenManager";


function ClientManager({client, onBack}) {
    const dispatch = useDispatch();

    const [currentTab, setCurrentTab] = useState(0);

    useEffect(() => {
        setCurrentTab(0);
    }, [client]);

    const handleChange = (event, newValue) => {
        setCurrentTab(newValue);
    };

    return (
        <Paper sx={{height: "100%", overflow:'hidden', mb:1}} elevation={10}>
            <IconButton onClick={onBack} sx={{mt: 1, ml: 1}}>
                <ArrowBack/>
            </IconButton>
            <Tabs value={currentTab} onChange={handleChange}>
                <MuiTab label="Information"/>
                <MuiTab label="File manager"/>
                <MuiTab label="Webcam manager"/>
                <MuiTab label="Screen manager"/>
            </Tabs>

            {currentTab === 0 && (
                <div>
                    {client.systemInformation.UUID} - {client.systemInformation.USER_NAME}
                </div>
            )}
            {currentTab === 1 && <FileManager/>}
            {currentTab === 2 && <WebcamManager/>}
            {currentTab === 3 && <ScreenManager/>}
        </Paper>
    );
}

export default ClientManager;
