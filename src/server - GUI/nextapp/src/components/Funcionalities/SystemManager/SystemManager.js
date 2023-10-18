import React from 'react';
import {Box, Typography, Button, Divider, Tooltip, Grid, IconButton} from '@mui/material';
import {PowerSettingsNew, Refresh, Delete, RestartAlt, Stop, SensorsOff, Bedtime} from '@mui/icons-material';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import {useDispatch, useSelector} from "react-redux";
import { icon } from "leaflet"
import {DISCONNECT, RESTART, SYSTEM_STATE, UNINSTALL} from "@redux/actions/stateActions";

const ICON = icon({
    iconUrl: "/location-icon.png",
    iconSize: [25, 32],
})

function SystemInformation() {
    const dispatch = useDispatch();
    const client = useSelector(state => state.client.selectedClient);
    const position = [40.40184097835221, -3.738877154024566];

    return (
        <Box sx={{ display: 'flex', flexDirection: 'column', p: 2, maxHeight: "65vh", overflow: 'auto' }}>

            <Box sx={{ display: 'flex', flexDirection: 'row', height: '65vh',flexGrow: 1 }}>

                <Box sx={{ flex: 1, pr: 2, borderRadius: '20px' }}>
                    <MapContainer center={position} zoom={13} style={{ width: '100%', height: '100%' ,borderRadius: '20px'}}>
                        <TileLayer
                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                        />
                        <Marker icon={ICON}  position={position}>
                            <Popup>
                                Client position (10km accuracy)
                            </Popup>
                        </Marker>
                    </MapContainer>
                </Box>

                <Divider orientation="vertical" flexItem />

                <Box sx={{ flex: 1, pl: 2, display: 'flex', flexDirection: 'column' }}>

                    {/* Información del sistema */}
                    <Box sx={{ flexGrow: 1 }}>
                        <Typography variant="h6">System information</Typography>
                        <Divider sx={{mb:1, mt:1}}/>
                        <Grid container spacing={1}>
                            <Grid item xs={4}>
                                <Typography variant="caption">UUID</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <Typography variant="caption">{client.systemInformation.UUID}</Typography>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography variant="caption">IP</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <Typography variant="caption">{client.networkInformation.IP}</Typography>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography variant="caption">Internet company</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <Typography variant="caption">{client.networkInformation.INTERNET_COMPANY_NAME}</Typography>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography variant="caption">Username</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <Typography variant="caption">{client.systemInformation.USER_NAME}</Typography>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography variant="caption">Operating system:</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <Typography variant="caption">{client.systemInformation.OPERATING_SYSTEM}</Typography>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography variant="caption">Continent</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <Typography variant="caption">{client.networkInformation.USER_CONTINENT}</Typography>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography variant="caption">Country</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <Typography variant="caption">{client.networkInformation.USER_COUNTRY}</Typography>
                            </Grid>
                            <Grid item xs={4}>
                                <Typography variant="caption">City</Typography>
                            </Grid>
                            <Grid item xs={8}>
                                <Typography variant="caption">{client.networkInformation.USER_CITY}</Typography>
                            </Grid>



                        </Grid>
                    </Box>

                    {/* Botones */}
                    <Box sx={{ display: 'flex', flexDirection: 'row', gap: 2, mt: 3 }}>
                        <Tooltip title="Disconnect">
                            <IconButton onClick={() => dispatch({type:DISCONNECT})}>
                                <SensorsOff/>
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Restart connection" onClick={() => dispatch({type:RESTART})}>
                            <IconButton >
                                <Refresh />
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Uninstall system">
                            <IconButton onClick={() => dispatch({type:UNINSTALL})}>
                                <Delete />
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Sleep system">
                            <IconButton onClick={() => dispatch({type:SYSTEM_STATE, payload: 0})}>
                                <Bedtime/>
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Restart system">
                            <IconButton onClick={() => dispatch({type:SYSTEM_STATE, payload: 2})}>
                                <RestartAlt />
                            </IconButton>
                        </Tooltip>
                        <Tooltip title="Shutdown system">
                        <IconButton >
                            <PowerSettingsNew onClick={() => dispatch({type:SYSTEM_STATE, payload: 1})}/>
                        </IconButton>
                    </Tooltip>

                    </Box>

                    </Box>
            </Box>

        </Box>
    );
}

export default SystemInformation;
