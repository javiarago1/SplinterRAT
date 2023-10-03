import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Table, TableBody, TableCell, TableHead, TableRow, CircularProgress } from '@mui/material';
import FolderIcon from '@mui/icons-material/Folder';
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile';
import { REQUEST_DIRECTORY } from "./fileManagerActions";

const FileTable = () => {
    const dispatch = useDispatch(); // Hook de Redux para despachar acciones
    const stack = useSelector((state) => state.client.directoryStack);
    const directoryData = stack[stack.length - 1];
    const selectedClient = useSelector(state => state.client.selectedClient);

    if (!directoryData || directoryData.visited) {
        return (
            <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' }}>
                <CircularProgress />
            </div>
        );
    }

    // Manejador para el doble clic en una fila de la tabla
    const handleRowDoubleClick = (path) => {
        let prePath = "";
        if (stack.length > 0) prePath = stack[stack.length - 1].requested_directory + "\\";
        const finalPath = prePath + path;
        dispatch({type: REQUEST_DIRECTORY, payload: { client_id : selectedClient.systemInformation.UUID , path : finalPath}});
    };

    return (
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
                    >
                        <TableCell>
                            <FolderIcon /> {folderName}
                        </TableCell>
                        <TableCell></TableCell>
                    </TableRow>
                ))}
                {directoryData.files.map((file) => (
                    <TableRow
                        key={file.name}
                        data-type="file"
                    >
                        <TableCell>
                            <InsertDriveFileIcon /> {file.name}
                        </TableCell>
                        <TableCell>{file.size}</TableCell>
                    </TableRow>
                ))}
            </TableBody>
        </Table>
    );
};

export default FileTable;
