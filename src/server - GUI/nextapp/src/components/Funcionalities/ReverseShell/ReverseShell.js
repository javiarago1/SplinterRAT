import React, { useState, useRef, useEffect } from 'react';
import {Box, TextareaAutosize, TextField, Container, CircularProgress} from '@mui/material';
import {useDispatch, useSelector} from "react-redux";
import {REVERSE_SHELL_COMMAND, START_REVERSE_SHELL} from "@redux/actions/reverseShellActions";

const ReverseShell = () => {
    const dispatch= useDispatch();
    const { command } = useSelector(state => state.reverseShell);
    const [currentCommand, setCurrentCommand] = useState('');
    const textAreaRef = useRef(null);

    useEffect(() => {
        dispatch({type: START_REVERSE_SHELL});
    }, [dispatch]);

    useEffect(() => {
        if (textAreaRef.current) {
            textAreaRef.current.scrollTop = textAreaRef.current.scrollHeight;
        }
    }, [command]);
    const handleCommandSubmit = (e) => {
        e.preventDefault();
        console.log(e.target.value);
        dispatch({type: REVERSE_SHELL_COMMAND, payload: currentCommand});
        setCurrentCommand('');
    };

    if (command === null) return <CircularProgress />

    return (
            <Box
                sx={{
                    p: 2,
                    height: '65vh',
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'space-between',
                    overflow: 'auto'
                }}
            >
                {/* Output */}
                <TextareaAutosize
                    ref={textAreaRef}
                    value={command}
                    style={{
                        minHeight: '50vh',
                        width: '100%',
                        backgroundColor: 'black',
                        color: 'green',
                        padding: '1em',
                        resize: 'none',
                        overflow: 'auto',
                        marginBottom: '1em'
                    }}
                    readOnly
                />

                {/* Textfield para comandos */}
                <form onSubmit={handleCommandSubmit} style={{width: '100%'}}>
                    <TextField
                        fullWidth
                        variant="outlined"
                        placeholder="Enter command"
                        value={currentCommand}
                        onChange={(e) => setCurrentCommand(e.target.value)}
                    />
                </form>
            </Box>
    );
};

export default ReverseShell;
