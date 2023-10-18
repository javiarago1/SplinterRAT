import {createSlice} from "@reduxjs/toolkit";

const compilerSlice = createSlice({
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
} = compilerSlice.actions;


export const compilerReducer = compilerSlice.reducer;