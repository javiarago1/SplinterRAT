import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import {SHOW_MESSAGE_BOX} from "@redux/actions/messsageBoxActions";
import {setButtonType, setContent, setIconType, setTitle} from "@redux/slices/messageBoxSlice";
import {Box, Button, FormControl, InputLabel, MenuItem, Select, TextField} from "@mui/material";


function MessageBox() {
    const dispatch = useDispatch();
    const {title, content, buttonType, iconType } = useSelector(state => state.messageBox);
    const typeIndex = ['Ok', 'Ok, cancel', 'Yes, no', 'Yes, no, cancel'].indexOf(buttonType);
    const iconIndex = ['Error', 'Question', 'Warning', 'Packets'].indexOf(iconType);


    const handleSend = () => {
        const message = {
            title: title,
            content: content,
            type: typeIndex,
            icon: iconIndex
        };
        dispatch({type: SHOW_MESSAGE_BOX, payload: {info: message}});
    };

    return (
        <Box sx={{ p: 2, display: 'flex', flexDirection: 'column', gap: 1, maxHeight: '65vh', overflow: 'auto'}}>
            <TextField
                fullWidth
                sx={{mb:2}}
                label="Title for message box"
                value={title}
                onChange={(e) => dispatch(setTitle(e.target.value))}
            />

            <TextField
                fullWidth
                multiline
                rows={4}
                label="Content for message box"
                value={content}
                onChange={(e) => dispatch(setContent(e.target.value))}
            />

            <FormControl variant="outlined" fullWidth margin="normal">
                <InputLabel id="button-type-label">Select button</InputLabel>
                <Select
                    labelId="button-type-label"
                    value={buttonType}
                    onChange={(e) => dispatch(setButtonType(e.target.value))}
                    label="Select button /"
                >
                    <MenuItem value="Ok">Ok</MenuItem>
                    <MenuItem value="Ok, cancel">Ok, cancel</MenuItem>
                    <MenuItem value="Yes, no">Yes, no</MenuItem>
                    <MenuItem value="Yes, no, cancel">Yes, no, cancel</MenuItem>
                </Select>
            </FormControl>


            <FormControl fullWidth margin ="normal">
                <InputLabel id="label-icon-type">Select button </InputLabel>
                <Select
                    value={iconType}
                    label="Select button /"
                    labelId="demo-simple-select-disabled-label"
                    id="demo-simple-select-disabled"
                    onChange={(e) => dispatch(setIconType(e.target.value))}


                >
                    <MenuItem value="Error">Error</MenuItem>
                    <MenuItem value="Question">Question</MenuItem>
                    <MenuItem value="Warning">Warning</MenuItem>
                    <MenuItem value="Packets">Packets</MenuItem>
                </Select>
            </FormControl>

            <Button disabled={buttonType.length === 0 || iconType.length === 0}
                variant="contained"
                color="primary"
                onClick={handleSend}
            >
                Send box
            </Button>
        </Box>
    );
}

export default MessageBox;
