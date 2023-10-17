import React, { useState } from 'react';
import {Button, TextField, TextareaAutosize, Divider, Select, MenuItem, ListItem, List, Box} from '@mui/material';

function KeyboardController() {
    const [delay, setDelay] = useState("");
    const [textToWrite, setTextToWrite] = useState("");
    const [specialKey, setSpecialKey] = useState("");
    const [events, setEvents] = useState([]);
    const [selectedEventIndex, setSelectedEventIndex] = useState(null);

    const handleAddDelay = () => {
        setEvents(prevEvents => [...prevEvents, `Delay: ${delay} ms`]);
        setDelay("");
    };

    const handleAddText = () => {
        setEvents(prevEvents => [...prevEvents, `Text to write: ${textToWrite}`]);
        setTextToWrite("");
    };

    const handleAddSpecialKey = () => {
        setEvents(prevEvents => [...prevEvents, `Special key: ${specialKey}`]);
    };

    const handleMoveUp = () => {
        if (selectedEventIndex > 0) {
            const newEvents = [...events];
            const temp = newEvents[selectedEventIndex - 1];
            newEvents[selectedEventIndex - 1] = newEvents[selectedEventIndex];
            newEvents[selectedEventIndex] = temp;
            setEvents(newEvents);
            setSelectedEventIndex(selectedEventIndex - 1);
        }
    };

    const handleMoveDown = () => {
        if (selectedEventIndex < events.length - 1) {
            const newEvents = [...events];
            const temp = newEvents[selectedEventIndex + 1];
            newEvents[selectedEventIndex + 1] = newEvents[selectedEventIndex];
            newEvents[selectedEventIndex] = temp;
            setEvents(newEvents);
            setSelectedEventIndex(selectedEventIndex + 1);
        }
    };

    const handleRemoveEvent = () => {
        const newEvents = [...events];
        newEvents.splice(selectedEventIndex, 1);
        setEvents(newEvents);
        setSelectedEventIndex(null);
    };

    const transformEventsToJson = (events) => {
        return events.map(event => {
            if (event.startsWith("Delay:")) {
                const value = parseInt(event.split(" ")[1], 10); // Extract the delay value
                return {
                    type: "jcDelay",
                    value: value
                };
            }
            else if (event.startsWith("Text to write:")) {
                const value = event.split(": ")[1]; // Extract the text to write
                return {
                    type: "text",
                    value: value
                };
            }
            else if (event.startsWith("Special key:")) {
                const value = event.split(": ")[1]; // Extract the special key name
                return {
                    type: "jcOrder",
                    value: value // You might want to map this value to specific key codes later on
                };
            }
        });
    }

    const handleSubmit = () => {
        const commandSequence = transformEventsToJson(events);
        const commandSequenceJson = JSON.stringify(commandSequence);
        console.log(commandSequenceJson);

        // Here, you can send 'commandSequenceJson' to your C++ client
    };

    return (
        <Box sx={{ display: "flex", width: "100%", maxHeight:'65vh', overflow:'auto'}}>
            <Box sx={{ flex: 1, padding: 2 , display: 'flex', flexDirection: 'column' }}>
                {/* La lista ocupará todo el espacio disponible */}
                <List sx={{ flexGrow: 1, overflowY: 'auto' }}>
                    {events.map((event, index) => (
                        <ListItem
                            key={index}
                            selected={selectedEventIndex === index}
                            onClick={() => setSelectedEventIndex(index)}
                        >
                            {event}
                        </ListItem>
                    ))}
                </List>

                {/* El Box de los botones tendrá un tamaño basado en su contenido */}
                <Box sx={{ flexShrink: 0, display: 'flex', gap: '8px', alignItems: 'center', justifyContent: 'space-between' }}>
                    <Button onClick={handleMoveUp} disabled={selectedEventIndex === null || selectedEventIndex === 0}>Up</Button>
                    <Button onClick={handleMoveDown} disabled={selectedEventIndex === null || selectedEventIndex === events.length - 1}>Down</Button>
                    <Button onClick={handleRemoveEvent} disabled={selectedEventIndex === null}>Remove</Button>
                    <Button onClick={() => setEvents([])}>Clear All</Button>
                </Box>
            </Box>

            <Box sx={{ flex: 1, m: 3, display: 'flex', flexDirection: 'column', gap: '10px' }}>
                <TextField type="number" label="Delay (ms)" value={delay} onChange={(e) => setDelay(e.target.value)} fullWidth />
                <Button variant="outlined" onClick={handleAddDelay} fullWidth>Add delay</Button>

                <TextField
                    label="Add text to be written"
                    multiline
                    rows={3}
                    value={textToWrite}
                    onChange={(e) => setTextToWrite(e.target.value)}
                    fullWidth
                    variant="outlined"
                />
                <Button variant="outlined" onClick={handleAddText} fullWidth>Add text</Button>

                <Select
                    fullWidth
                    value={specialKey}
                    onChange={(e) => setSpecialKey(e.target.value)}
                >
                    <MenuItem value={"Enter key"}>Enter key</MenuItem>
                    <MenuItem value={"Left Windows key"}>Left Windows key</MenuItem>
                    <MenuItem value={"Tab key"}>Tab key</MenuItem>
                </Select>

                <Button variant="outlined" onClick={handleAddSpecialKey} fullWidth>Add to list</Button>
                <Divider/>
                <Button onClick={handleSubmit} variant="contained" color="primary" fullWidth>Submit request to client</Button>
            </Box>
        </Box>
    );
}

export default KeyboardController;
