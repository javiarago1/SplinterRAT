import { createTheme } from '@mui/material/styles';
import NextLink from "next/link";
import {forwardRef} from "react";

const LinkBehaviour = forwardRef(function LinkBehaviour(props, ref) {
    return <NextLink ref={ref} {...props} />;
});


const darkTheme = createTheme({
    palette: {
        mode: 'dark',
        primary: {
            main: '#c4c4cb',
            dark: '#757575',
            light: '#343434',
        },
        secondary: {
            main: '#FF8A85',
            dark: '#BE5E5A',
            light: '#FFB4B2',
        },
        error: {
            main: '#FF5C5C',
        },
        warning: {
            main: '#FFC75C',
        },
        info: {
            main: '#5CC6FF',
        },
        success: {
            main: '#5CFF95',
        },
        background: {
            default: '#383838',
            paper: '#1c1c1c',
        },
        text: {
            primary: '#E0E0E0',
            secondary: '#B3B3B3',
        },
    },
    typography: {
        fontFamily: 'Arial, sans-serif',
        fontWeightLight: 300,
        fontWeightRegular: 400,
        fontWeightMedium: 500,
        fontWeightBold: 700,
    },
    components: {
        MuiLink: {
            defaultProps: {
                underline: 'none',
                component: LinkBehaviour
            },
            styleOverrides: {
                root: {
                    width: '100%',
                },
            },
        },
        MuiButtonBase: {
            defaultProps: {
                LinkComponent: LinkBehaviour
            }
        }
    }
});

export default darkTheme;
