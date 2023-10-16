import {createSlice} from "@reduxjs/toolkit";
import reverseShell from "@components/Funcionalities/ReverseShell/ReverseShell";

const revereShellSlice = createSlice({
    name: 'revereShell',
    initialState: {
        command: "",
    },
    reducers: {
        setCommandResponse: (state, action) => {
            state.command += action.payload.result;
        },

    }
});

export const {
    setCommandResponse
} = revereShellSlice.actions;


export const reverseShellReducer = revereShellSlice.reducer;