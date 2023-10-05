import React from 'react';
import { Provider } from 'react-redux';
import { store } from '@redux/store/store';
import { ThemeProvider } from "@mui/material";
import darkTheme from "../src/theme/theme";

function MyApp({ Component, pageProps }) {
    return (
        <Provider store={store}>
            <ThemeProvider theme={darkTheme}>
                <Component {...pageProps} />
            </ThemeProvider>
        </Provider>
    );
}

export default MyApp;
