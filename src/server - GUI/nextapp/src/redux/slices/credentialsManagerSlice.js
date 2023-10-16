import {createSlice} from "@reduxjs/toolkit";

const credentialsManagerSlice = createSlice({
    name: 'revereShell',
    initialState: {
        credentials: null,
    },
    reducers: {
        setCredentials: (state, action) => {
            state.credentials = action.payload.info;
        },

    }
});

export const {
    setCredentials
} = credentialsManagerSlice.actions;


export const credentialsManagerReducer = credentialsManagerSlice.reducer;