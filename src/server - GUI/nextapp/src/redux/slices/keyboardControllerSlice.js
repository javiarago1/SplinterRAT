import {createSlice} from "@reduxjs/toolkit";
import reverseShell from "@components/Funcionalities/ReverseShell/ReverseShell";

const keyboardControllerSlice = createSlice({
    name: 'keyboardController',
    initialState: {
        delayValue: "",
        textValue: "",
        specialKeyValue: "",
        listOfEvents: [],
        selectedEventIndex: null,
    },
    reducers: {
        setDelayValue: (state, action) => {
            state.delayValue = action.payload;
        },
        setTextValue: (state, action) => {
            state.textValue = action.payload;
        },
        setSpecialKeyValue: (state, action) => {
            state.specialKeyValue = action.payload;
        },
        addToListOfEvents: (state, action) => {
            state.listOfEvents = action.payload;
        },
        setSelectedEventIndex: (state, action) => {
            state.selectedEventIndex = action.payload;
        }
    }
});

export const {
    setDelayValue,
    setTextValue,
    setSpecialKeyValue,
    addToListOfEvents,
    setSelectedEventIndex
} = keyboardControllerSlice.actions;


export const keyboardControllerReducer = keyboardControllerSlice.reducer;