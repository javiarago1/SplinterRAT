import React from 'react';
import {useSelector} from 'react-redux';
import {Table, TableBody, TableCell, TableHead, TableRow, Paper} from '@mui/material';
import styles from './ClienTable.module.css'

function ClientTable({onClientSelect}) {
    const clients = useSelector(state => state.client.clients);

    return (
        <Paper elevation={10}>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>UUID</TableCell>
                        <TableCell>IP</TableCell>
                        <TableCell>Country</TableCell>
                        <TableCell>Tag</TableCell>
                        <TableCell>Username</TableCell>
                        <TableCell>Operating system</TableCell>
                        <TableCell>Status</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {Object.values(clients).map(client => (
                        <TableRow
                            key={client.systemInformation.UUID}
                            onClick={() => onClientSelect(client)}
                            className={styles.clientRow}
                        >
                            <TableCell>{client.systemInformation.UUID}</TableCell>
                            <TableCell>{client.networkInformation.IP}</TableCell>
                            <TableCell>{client.networkInformation.USER_COUNTRY}</TableCell>
                            <TableCell>{client.systemInformation.TAG_NAME}</TableCell>
                            <TableCell>{client.systemInformation.USER_NAME}</TableCell>
                            <TableCell>{client.systemInformation.OPERATING_SYSTEM}</TableCell>
                            <TableCell>
                                <span
                                    style={{color: client.isConnected ? 'green' : 'red'}}>{client.isConnected ? "Connected" : "Disconnected"}
                                </span>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </Paper>
    );
}

export default ClientTable;
