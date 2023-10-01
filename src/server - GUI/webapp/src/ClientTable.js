import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {addClientTab, selectClient} from './clientSlice';
import { Table, TableBody, TableCell, TableHead, TableRow, Paper } from '@mui/material';

function ClientTable({ setCurrentTab }) {
    // Obtener los clientes desde el estado de Redux
    const clients = useSelector(state => state.client.clients);
    const tabs = useSelector(state => state.client.tabs);

    const dispatch = useDispatch();

    const handleRowDoubleClick = (client) => {
        const clientUUID = client.systemInformation.UUID;
        dispatch(addClientTab({ uuid: clientUUID, name: client.systemInformation.USER_NAME, default: false }));
        dispatch(selectClient(client));


        const tabIndex = tabs.findIndex(c => c.uuid === clientUUID);
        setCurrentTab(tabIndex !== -1 ? tabIndex : tabs.length);
    };


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
                            onDoubleClick={() => handleRowDoubleClick(client)}
                        >
                            <TableCell>{client.systemInformation.UUID}</TableCell>
                            <TableCell>{client.networkInformation.IP}</TableCell>
                            <TableCell>{client.networkInformation.USER_COUNTRY}</TableCell>
                            <TableCell>{client.systemInformation.TAG_NAME}</TableCell>
                            <TableCell>{client.systemInformation.USER_NAME}</TableCell>
                            <TableCell>{client.systemInformation.OPERATING_SYSTEM}</TableCell>
                            <TableCell> Connected </TableCell>
                            {/* ... puedes agregar más celdas aquí según tus necesidades ... */}
                        </TableRow>)
                    })}
                </TableBody>
            </Table>
        </Paper>
    );
}

export default ClientTable;
