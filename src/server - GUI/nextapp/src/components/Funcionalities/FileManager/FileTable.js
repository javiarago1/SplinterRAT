import React, {useEffect, useRef, useState} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {Table, TableBody, TableCell, TableHead, TableRow, CircularProgress, Box} from '@mui/material';
import FolderIcon from '@mui/icons-material/Folder';
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile';
import {REQUEST_DIRECTORY} from "@redux/actions/fileManagerActions";
import { selectRow, clearSelectedRows , deselectRow} from '@redux/slices/fileManagerSlice'

const FileTable = () => {
    const dispatch = useDispatch();
    const stack = useSelector((state) => state.fileManager.directoryStack);
    const directoryData = stack[stack.length - 1];
    const selectedClient = useSelector(state => state.client.selectedClient);
    const selectedRows = useSelector(state => state.fileManager.selectedRows);
    const currentDirectory = useSelector(state => state.fileManager.currentDirectory);
    const containerRef = useRef();


    useEffect(() => {
        if (containerRef.current) {
            containerRef.current.scrollTop = 0;
        }
    }, [directoryData]);

    if (!directoryData || directoryData.visited) {
        return (
            <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '360px'}}>
                <CircularProgress/>
            </div>
        );
    }

    const handleRowDoubleClick = (path) => {
        const finalPath = currentDirectory + path + "\\";
        dispatch({
            type: REQUEST_DIRECTORY,
            payload: {client_id: selectedClient.systemInformation.UUID, path: finalPath}
        });
        dispatch(clearSelectedRows());
    };

    const handleRowClick = (e, name, type) => {
        if (e.ctrlKey) {
            if (selectedRows.some(row => row.name === name && row.type === type)) {
                dispatch(deselectRow({ name, type }));
            } else {
                dispatch(selectRow({ name, type }));
            }
        } else {
            dispatch(clearSelectedRows());
            dispatch(selectRow({ name, type }));
        }
    };

    return (
        <Box
            sx={{
                maxHeight: '360px',
                overflowY: 'auto',
                '&::-webkit-scrollbar': {
                    width: '0.4em'
                },
                '&::-webkit-scrollbar-track': {
                    boxShadow: 'inset 0 0 6px rgba(0,0,0,0.00)',
                    webkitBoxShadow: 'inset 0 0 6px rgba(0,0,0,0.00)'
                },
                '&::-webkit-scrollbar-thumb': {
                    backgroundColor: 'rgba(0,0,0,.1)',
                    outline: '1px solid slategrey'
                }
            }}
        >
        <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Name</TableCell>
                        <TableCell>Size</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {directoryData.folders.map((folderName) => (
                        <TableRow
                            key={folderName}
                            data-type="folder"
                            onDoubleClick={() => handleRowDoubleClick(folderName)}
                            onClick={(e) => handleRowClick(e, folderName, 'folder')}
                            selected={selectedRows.some(row => row.name === folderName && row.type === 'folder')}
                        >
                            <TableCell>
                                <FolderIcon/> {folderName}
                            </TableCell>
                            <TableCell></TableCell>
                        </TableRow>
                    ))}
                    {directoryData.files.map((file) => (
                        <TableRow
                            key={file.name}
                            data-type="file"
                            onClick={(e) => handleRowClick(e, file.name, 'file')}
                            selected={selectedRows.some(row => row.name === file.name && row.type === 'file')}
                        >
                            <TableCell>
                                <InsertDriveFileIcon/> {file.name}
                            </TableCell>
                            <TableCell>{file.size}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </Box>
    );
};

export default FileTable;
