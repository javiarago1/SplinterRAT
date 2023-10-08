import React from 'react';
import { Provider } from 'react-redux';
import { CssBaseline, ThemeProvider, Drawer, Toolbar, AppBar, Typography, Box } from '@mui/material';
import darkTheme from "../src/theme/theme";
import Navigation from "@components/Navigation/Navigation";
import { store } from '@redux/store/store';

function MyApp({ Component, pageProps }) {
    return (
        <Provider store={store}>
            <ThemeProvider theme={darkTheme}>
                <CssBaseline />
                <Box sx={{ display: 'flex' }}>
                    <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
                        <Toolbar>
                            <Typography variant="h6" noWrap>
                                SplinterRAT
                            </Typography>
                        </Toolbar>
                    </AppBar>
                    <Drawer
                        variant="permanent"
                        sx={{
                            width: 240,
                            flexShrink: 0,
                            [`& .MuiDrawer-paper`]: { width: 240, boxSizing: 'border-box' },
                        }}
                    >
                        <Toolbar />
                        <Navigation />
                    </Drawer>
                    <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
                        <Toolbar />
                        <Component {...pageProps} />
                    </Box>
                </Box>
            </ThemeProvider>
        </Provider>
    );
}

export default MyApp;
