import {createSlice} from "@reduxjs/toolkit";
import reverseShell from "@components/Funcionalities/ReverseShell/ReverseShell";

const messageBoxSlice = createSlice({
    name: 'messageBox',
    initialState: {
        title: "",
        content: "",
        buttonType: "",
        iconType: "",
    },
    reducers: {
        setTitle: (state, action) => {
            state.title = action.payload;
        },
        setContent: (state, action) => {
            state.content = action.payload;
        },
        setButtonType: (state, action) => {

            state.buttonType = action.payload;
        },
        setIconType: (state, action) => {
            state.iconType = action.payload;
        },
    }
});

export const {
    setTitle,
    setContent,
    setButtonType,
    setIconType
} = messageBoxSlice.actions;


export const messageBoxReducer = messageBoxSlice.reducer;