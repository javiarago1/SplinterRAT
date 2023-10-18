import React from 'react';
import {Provider} from 'react-redux';
import {CssBaseline, ThemeProvider, AppBar, Toolbar, Box, Paper} from '@mui/material';
import Image from 'next/image';
import darkTheme from "../src/theme/theme";
import Navigation from "@components/Navigation/Navigation";
import {store} from '@redux/store/store';

function MyApp({Component, pageProps}) {
    return (
        <Provider store={store}>
            <ThemeProvider theme={darkTheme}>
                <CssBaseline/>
                <Box sx={{display: 'flex', flexDirection: 'column', height: '100vh'}}>
                    {/* AppBar Section */}
                    <Box component="header" sx={{flexShrink: 0}}>
                        <AppBar position="static">
                            <Toolbar sx={{display: 'flex', justifyContent: 'center', padding: 1}}>
                                <Image src="/logo.png" alt="Empresa Logo" width="100" height="61"/>
                            </Toolbar>
                        </AppBar>
                    </Box>

                    {/* Content Section */}

                    <Box sx={{display: 'flex', flexGrow: 1, overflow: 'auto'}}>

                        {/* Navigation Drawer */}
                        <Box

                            component="nav"
                            sx={{
                                display: {xs: 'none', sm: 'block'},
                                ml: 2,
                                mt: 2,
                                width: '15%',
                            }}
                        >
                            <Paper elevation={10}>
                                <Navigation/>
                            </Paper>
                        </Box>


                        {/* Main Content */}
                        <Box component="main" height="86vh" sx={{flexGrow: 1, p: 2}}>
                            <Component {...pageProps} />
                        </Box>
                    </Box>
                </Box>
            </ThemeProvider>
        </Provider>
    );
}

export default MyApp;
