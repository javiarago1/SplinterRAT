
import React, { useState } from 'react';
import {
    Button,
    Checkbox,
    Tab,
    Tabs,
    Box,
    TextField,
    Select,
    Typography,
    MenuItem,
    Paper,
    Grid,
    Divider, FormControlLabel
} from '@mui/material';

function CompilerGUI() {
    // Estados para los checkboxes y otros componentes interactivos
    const [webcamChecked, setWebcamChecked] = useState(false);
    const [keyloggerChecked, setKeyloggerChecked] = useState(false);
    const [tag, setTag] = useState("");
    const [mutex, setMutex] = useState("");
    const [ip, setIp] = useState("");
    const [port, setPort] = useState("");
    const [timing, setTiming] = useState("");

    // Estado para las pestañas
    const [currentTab, setCurrentTab] = useState(0);

    return (
        <Paper elevation={10} sx={{height:'80vh', maxHeight: '80vh', overflow:'auto', maxWidth: '100%'}}>
            {/* Pestañas */}
            <Tabs value={currentTab} onChange={(event, newValue) => setCurrentTab(newValue)}>
                <Tab label="Identification" />
                <Tab label="Installation" />
                <Tab label="Modules" />
                <Tab label="Assembly" />
                <Tab label="Compile" />
            </Tabs>

            {/* Contenido de las pestañas */}
            <Box sx={{ maxHeight:'65vh', width:'100%' }}>
                {currentTab === 0 && (
                    <Grid container>
                        {/* Identification Fields */}
                        <Grid item xs={12}>
                            <Typography variant="subtitle1">Identification:</Typography>
                            <Divider sx={{ margin: '0.5rem 0' }} />
                        </Grid>
                        <Grid item xs={6} md={4}>
                            <Box mb={2}>
                                <Typography variant="body1">Tag:</Typography>
                                <TextField fullWidth />
                            </Box>
                        </Grid>
                        <Grid item xs={6} md={4}>
                            <Box mb={2}>
                                <Typography variant="body1">Mutex:</Typography>
                                <TextField fullWidth />
                            </Box>
                        </Grid>
                        <Grid item xs={6} md={4}>
                            <Box mb={2}>
                                <Typography variant="body1">IP:</Typography>
                                <TextField fullWidth />
                            </Box>
                        </Grid>
                        <Grid item xs={6} md={4}>
                            <Box mb={2}>
                                <Typography variant="body1">Port:</Typography>
                                <TextField fullWidth />
                            </Box>
                        </Grid>
                        <Grid item xs={6} md={4}>
                            <Box mb={2}>
                                <Typography variant="body1">Timing:</Typography>
                                <TextField fullWidth />
                            </Box>
                        </Grid>

                        {/* Installation Fields */}
                        <Grid item xs={12}>
                            <Typography variant="subtitle1">Installation:</Typography>
                            <Divider sx={{ margin: '0.5rem 0' }} />
                        </Grid>
                        <Grid item xs={6} md={4}>
                            <Box mb={2}>
                                <Typography variant="body1">Nombre del archivo:</Typography>
                                <TextField fullWidth />
                            </Box>
                        </Grid>
                        <Grid item xs={6} md={4}>
                            <Box mb={2}>
                                <Typography variant="body1">Carpeta de instalación:</Typography>
                                <TextField fullWidth />
                            </Box>
                        </Grid>
                        <Grid item xs={6} md={4}>
                            <Box mb={2}>
                                <Typography variant="body1">Subcarpeta:</Typography>
                                <TextField fullWidth />
                            </Box>
                        </Grid>
                        <Grid item xs={6} md={4}>
                            <FormControlLabel
                                control={<Checkbox />}
                                label="Visibilidad"
                            />
                        </Grid>
                        <Grid item xs={6} md={4}>
                            <FormControlLabel
                                control={<Checkbox />}
                                label="Persistencia"
                            />
                        </Grid>
                    </Grid>
                )}
                {currentTab === 1 && (
                    <div>
                        {/* Contenido del tab Installation */}
                    </div>
                )}
                {currentTab === 2 && (
                    <div>
                        {/* Contenido del tab Modules */}
                    </div>
                )}
                {currentTab === 3 && (
                    <div>
                        {/* Contenido del tab Assembly */}
                    </div>
                )}
                {currentTab === 4 && (
                    <div>
                        {currentTab === 4 && (
                            <div>
                                <Typography variant="h6">
                                    Path where g++ and windres is located if it isnt in the system variables:
                                </Typography>
                                <Select
                                    value={"compilerOption"}
                                    onChange={(event) => setCompilerOption(event.target.value)}
                                >
                                    <MenuItem value="Default system compiler">Default system compiler</MenuItem>
                                    <MenuItem value="Select custom path">Select custom path</MenuItem>
                                </Select>
                                {"compilerOption" === "Select custom path" && (
                                    <TextField
                                        label="Compiler Path"
                                        value={"compilerPath"}
                                        onChange={(event) => setCompilerPath(event.target.value)}
                                    />
                                )}
                            </div>
                        )}
                    </div>
                )}
            </Box>
        </Paper>
    );
}

export default CompilerGUI;
