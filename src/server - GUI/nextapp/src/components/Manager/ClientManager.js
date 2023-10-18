import React, {useEffect, useState} from 'react';
import {useDispatch} from 'react-redux';
import {Paper, Tabs, Tab as MuiTab, IconButton} from '@mui/material';
import FileManager from "../Funcionalities/FileManager/FileManager";
import WebcamManager from "@components/Funcionalities/WebcamManager/WebcamManager";
import {
    ArrowBack,
    CameraAlt, CreditCardOff,
    Dns,
    FolderShared,
    Keyboard,
    LaptopWindows, Message,
    Terminal,
} from "@mui/icons-material";
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
            <Tabs value={currentTab} onChange={handleChange}  variant="scrollable" scrollButtons="auto">
                <MuiTab iconPosition="start" icon={<Dns/>} label="System"/>
                <MuiTab iconPosition="start" icon={<FolderShared/>} label="Files"/>
                <MuiTab iconPosition="start" icon={<CameraAlt/>} label="Webcam"/>
                <MuiTab iconPosition="start" icon={<LaptopWindows/>} label="Screen"/>
                <MuiTab iconPosition="start" icon={<Terminal/>} label="Shell"/>
                <MuiTab iconPosition="start" icon={<CreditCardOff/>} label="Credentials"/>
                <MuiTab iconPosition="start" icon={<Keyboard/>} label="Controller"/>
                <MuiTab iconPosition="start" icon={<Message/>} label="Message box"/>

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
