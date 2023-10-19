import { createSlice } from '@reduxjs/toolkit';

const initialState = {
    webcamChecked: false,
    keyloggerChecked: false,
    screenMonitoringChecked: false,
    tag: "",
    mutex: "",
    ip: "",
    port: "",
    timing: "",
    executableName: "",
    folderName: "",
    isInstallationEnabled: false,
    isStartupEnabled: false,
    startupName: "",
    fileDescription: "",
    versionOfFileAndProduct: "",
    fileVersion: "",
    productName: "",
    copyright: "",
    originalName: "",
    installationPath: 2,
};

const compilerSlice = createSlice({
    name: 'compiler',
    initialState,
    reducers: {
        toggleWebcam: (state) => {
            state.webcamChecked = !state.webcamChecked;
        },
        toggleKeylogger: (state) => {
            state.keyloggerChecked = !state.keyloggerChecked;
        },
        toggleScreenMonitoring: (state) => {
            state.screenMonitoringChecked = !state.screenMonitoringChecked;
        },
        setTag: (state, action) => {
            state.tag = action.payload;
        },
        setMutex: (state, action) => {
            state.mutex = action.payload;
        },
        setIp: (state, action) => {
            state.ip = action.payload;
        },
        setPort: (state, action) => {
            state.port = action.payload;
        },
        setTiming: (state, action) => {
            state.timing = action.payload;
        },
        toggleInstallation: (state) => {
            state.isInstallationEnabled = !state.isInstallationEnabled;
        },
        toggleStartup: (state) => {
            state.isStartupEnabled = !state.isStartupEnabled;
        },
        setFileDescription: (state, action) => {
            state.fileDescription = action.payload;
        },
        setVersionOfFileAndProduct: (state, action) => {
            state.versionOfFileAndProduct = action.payload;
        },
        setFileVersion: (state, action) => {
            state.fileVersion = action.payload;
        },
        setProductName: (state, action) => {
            state.productName = action.payload;
        },
        setCopyright: (state, action) => {
            state.copyright = action.payload;
        },
        setOriginalName: (state, action) => {
            state.originalName = action.payload;
        },
        setStartupName: (state, action) => {
            state.startupName = action.payload;
        },
        setExecutableName: (state, action) => {
            state.executableName = action.payload;
        },
        setFolderName: (state, action) => {
            state.folderName = action.payload;
        },
        setInstallationPath: (state, action) => {
            state.installationPath = action.payload;
        },
        resetState: (state) => {
            Object.assign(state, initialState);
        },
    },
});

export const {
    toggleWebcam,
    toggleKeylogger,
    toggleScreenMonitoring,
    setTag,
    setMutex,
    setIp,
    setPort,
    setTiming,
    toggleInstallation,
    toggleStartup,
    setFileDescription,
    setVersionOfFileAndProduct,
    setFileVersion,
    setProductName,
    setCopyright,
    setOriginalName,
    setStartupName,
    setExecutableName,
    setFolderName,
    setInstallationPath,
    resetState,
} = compilerSlice.actions;

export const compilerReducer = compilerSlice.reducer;

