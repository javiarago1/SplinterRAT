import React, {useState} from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {Table, TableBody, TableCell, TableHead, TableRow, CircularProgress} from '@mui/material';
import FolderIcon from '@mui/icons-material/Folder';
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile';
import {REQUEST_DIRECTORY} from "./fileManagerActions";
import { selectRow, clearSelectedRows , deselectRow} from './clientSlice'

const FileTable = () => {
    const dispatch = useDispatch();
    const stack = useSelector((state) => state.fileManager.directoryStack);
    const directoryData = stack[stack.length - 1];
    const selectedClient = useSelector(state => state.client.selectedClient);
    const selectedRows = useSelector(state => state.fileManager.selectedRows);
    const currentDirectory = useSelector(state => state.fileManager.currentDirectory);


    if (!directoryData || directoryData.visited) {
        return (
            <div style={{display: 'flex', justifyContent: 'center', alignItems: 'center', height: '400px'}}>
                <CircularProgress/>
            </div>
        );
    }

    const handleRowDoubleClick = (path) => {
        let prePath = "";
        if (currentDirectory) prePath = currentDirectory + (stack.length > 1 ? "\\" : "");
        const finalPath = prePath + path;
        dispatch({
            type: REQUEST_DIRECTORY,
            payload: {client_id: selectedClient.systemInformation.UUID, path: finalPath}
        });
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
        <div style={{height: '400px', overflowY: 'auto'}}>
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
        </div>
    );
};

export default FileTable;
