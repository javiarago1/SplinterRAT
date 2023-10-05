import React from 'react';
import ReactDOM from 'react-dom/client';import App from './App';
import { Provider } from 'react-redux';
import { store } from './store';
import {ThemeProvider} from "@mui/material";
import darkTheme from "./theme";




const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(

    <Provider store={store}>
        <ThemeProvider theme={darkTheme}>
            <App />
        </ThemeProvider>
    </Provider>

);
