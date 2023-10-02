import React from 'react';
import { useSelector } from 'react-redux';
import { Table, TableBody, TableCell, TableHead, TableRow, Paper } from '@mui/material';

function ClientTable({ onClientSelect }) {
    const clients = useSelector(state => state.client.clients);

    return (
        <Paper>
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
                    {clients.map(client => {
                        return (<TableRow
                            key={client.systemInformation.UUID}
                            onDoubleClick={() => onClientSelect(client)}
                        >
                            <TableCell>{client.systemInformation.UUID}</TableCell>
                            <TableCell>{client.networkInformation.IP}</TableCell>
                            <TableCell>{client.networkInformation.USER_COUNTRY}</TableCell>
                            <TableCell>{client.systemInformation.TAG_NAME}</TableCell>
                            <TableCell>{client.systemInformation.USER_NAME}</TableCell>
                            <TableCell>{client.systemInformation.OPERATING_SYSTEM}</TableCell>
                            <TableCell> Connected </TableCell>
                        </TableRow>)
                    })}
                </TableBody>
            </Table>
        </Paper>
    );
}

export default ClientTable;
