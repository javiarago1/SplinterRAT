import React from 'react';
import {
    Box,
    Button,
    Divider,
    List,
    ListItem,
    ListItemButton,
    MenuItem,
    Select,
    TextField,
    Typography
} from '@mui/material';
import {useDispatch, useSelector} from "react-redux";
import {
    addToListOfEvents,
    setDelayValue,
    setSelectedEventIndex,
    setSpecialKeyValue,
    setTextValue
} from "@redux/slices/keyboardControllerSlice";
import {KEYBOARD_CONTROLLER} from "@redux/actions/keyboardControllerActions";

function KeyboardController() {
    const dispatch = useDispatch();
    const { delayValue, textValue, specialKeyValue, listOfEvents, selectedEventIndex } = useSelector(state => state.keyboardController);

    const handleAddDelay = () => {
        dispatch(addToListOfEvents([...(listOfEvents), `Delay: ${delayValue} ms`]));
    };

    const handleAddText = () => {
        dispatch(addToListOfEvents([...(listOfEvents), `Text to write: ${textValue}`]));
    };

    const handleAddSpecialKey = () => {
        dispatch(addToListOfEvents([...(listOfEvents), `Special key: ${specialKeyValue}`]));
    };

    const handleMoveUp = () => {
        if (selectedEventIndex > 0) {
            const newEvents = [...listOfEvents];
            const temp = newEvents[selectedEventIndex - 1];
            newEvents[selectedEventIndex - 1] = newEvents[selectedEventIndex];
            newEvents[selectedEventIndex] = temp;
            dispatch(addToListOfEvents(newEvents));
            dispatch(setSelectedEventIndex(selectedEventIndex - 1));
        }
    };

    const handleMoveDown = () => {
        if (selectedEventIndex < listOfEvents.length - 1) {
            const newEvents = [...listOfEvents];
            const temp = newEvents[selectedEventIndex + 1];
            newEvents[selectedEventIndex + 1] = newEvents[selectedEventIndex];
            newEvents[selectedEventIndex] = temp;
            dispatch(addToListOfEvents(newEvents));
            dispatch(setSelectedEventIndex(selectedEventIndex + 1));
        }
    };

    const handleRemoveEvent = () => {
        const newEvents = [...listOfEvents];
        newEvents.splice(selectedEventIndex, 1);
        dispatch(addToListOfEvents(newEvents));
        dispatch(setSelectedEventIndex(null));
    };

    const transformSpecialKeyToInt = (order) => {
        switch (order) {
            case "Enter key": {
                return 13;
            }
            case "Left Windows key": {
                return 91;
            }
            case "Tab key": {
                return 9;
            }
            default: {
                return 0;
            }
        }
    }

    const transformEventsToJson = (events) => {
        return events.map(event => {
            if (event.startsWith("Delay:")) {
                const value = parseInt(event.split(" ")[1], 10);
                return {
                    type: "jcDelay",
                    value: value
                };
            }
            else if (event.startsWith("Text to write:")) {
                const value = event.split(": ")[1];
                return {
                    type: "text",
                    value: value
                };
            }
            else if (event.startsWith("Special key:")) {
                const value = event.split(": ")[1];
                return {
                    type: "jcOrder",
                    value: transformSpecialKeyToInt(value)
                };
            }
        });
    }

    const handleSubmit = () => {
        const commandSequence = transformEventsToJson(listOfEvents);
        console.log(commandSequence);
        dispatch({type: KEYBOARD_CONTROLLER, payload: {command: commandSequence}});
    };

    return (
        <Box sx={{ display: "flex", width: "100%", maxHeight:'65vh', overflow:'auto'}}>
            <Box sx={{ flex: 1, padding: 2 , display: 'flex', flexDirection: 'column' }}>
                <List sx={{ flexGrow: 1, overflowY: 'auto' }}>
                    {listOfEvents.length > 0 ?
                        listOfEvents.map((event, index) => (
                            <ListItemButton
                                key={index}
                                selected={selectedEventIndex === index}
                                onClick={() => dispatch(setSelectedEventIndex(index))}
                                divider={true}
                            >
                                {event}
                            </ListItemButton>
                        ))
                        :
                        <Box sx={{ mt: 'auto', mb: 'auto', textAlign: 'left', pl: 1 }}>
                            <Typography variant="h6" gutterBottom>
                                Welcome to the Keyboard Controller!
                            </Typography>
                            <Typography variant="body2" sx={{ fontSize: '0.8rem', fontWeight: 'lighter', lineHeight: 1.5 }}>
                                This tool allows you to sequence and simulate keyboard actions. You can:
                            </Typography>
                            <List sx={{ fontSize: '0.8rem', fontWeight: 'lighter', lineHeight: 1.5, padding: 0, '& li': { paddingTop: 0, paddingBottom: 0, position: 'relative', paddingLeft: '1em' } }}>
                                <ListItem dense disableGutters sx={{ '&::before': { content: '"•"', position: 'absolute', left: 0, top: '50%', transform: 'translateY(-50%)' }}}>Type specific texts.</ListItem>
                                <ListItem dense disableGutters sx={{ '&::before': { content: '"•"', position: 'absolute', left: 0, top: '50%', transform: 'translateY(-50%)' }}}>Add delays between actions.</ListItem>
                                <ListItem dense disableGutters sx={{ '&::before': { content: '"•"', position: 'absolute', left: 0, top: '50%', transform: 'translateY(-50%)' }}}>Trigger special keys.</ListItem>
                            </List>
                            <Typography variant="body2" sx={{ fontSize: '0.8rem', fontWeight: 'lighter', lineHeight: 1.5 }}>
                                <br/>
                                Start by adding your desired events using the controls on the right.
                                <br/>
                                After building your sequence, you can send it to the client for execution.
                            </Typography>

                        </Box>
                    }
                </List>
                <Divider/>

                <Box sx={{ mt:2, flexShrink: 0, display: 'flex', gap: '2%', alignItems: 'center'}}>
                    <Button variant="outlined" onClick={handleMoveUp} disabled={selectedEventIndex === null || selectedEventIndex === 0}>Up</Button>
                    <Button variant="outlined" onClick={handleMoveDown} disabled={selectedEventIndex === null || selectedEventIndex === listOfEvents.length - 1}>Down</Button>
                    <Button variant="outlined" onClick={handleRemoveEvent} disabled={selectedEventIndex === null}>Remove</Button>
                    <Button disabled={listOfEvents.length === 0} variant="outlined" onClick={() => {
                        dispatch(addToListOfEvents([]))
                        dispatch(setSelectedEventIndex(null));
                    }}>Clear All</Button>
                </Box>
            </Box>

            <Box sx={{ flex: 1, m: 3, display: 'flex', flexDirection: 'column', gap: '10px' }}>
                <TextField type="number" label="Delay (ms)" value={delayValue} onChange={(e) => dispatch(setDelayValue(e.target.value))} fullWidth />
                <Button disabled={delayValue.length === 0} variant="outlined" onClick={handleAddDelay} fullWidth>Add delay</Button>

                <TextField
                    label="Add text to be written"
                    multiline
                    rows={3}
                    value={textValue}
                    onChange={(e) => dispatch(setTextValue(e.target.value))}
                    fullWidth
                    variant="outlined"
                />
                <Button disabled={textValue.length === 0} variant="outlined" onClick={handleAddText} fullWidth>Add text</Button>

                <Select
                    fullWidth
                    value={specialKeyValue}
                    onChange={(e) => dispatch(setSpecialKeyValue(e.target.value))}
                >
                    <MenuItem value={"Enter key"}>Enter key</MenuItem>
                    <MenuItem value={"Left Windows key"}>Left Windows key</MenuItem>
                    <MenuItem value={"Tab key"}>Tab key</MenuItem>
                </Select>

                <Button disabled={specialKeyValue.length === 0} variant="outlined" onClick={handleAddSpecialKey} fullWidth>Add to list</Button>
                <Divider/>
                <Button disabled = {listOfEvents.length === 0} onClick={handleSubmit} variant="contained" color="primary" fullWidth>Submit request to client</Button>
            </Box>
        </Box>
    );
}

export default KeyboardController;
