import React, { useEffect } from 'react';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';

const ContextMenu = ({ type, x, y, onClose }) => {
    // Funciones que se llaman al hacer clic en las diferentes opciones del menú
    const handleMenuClick = (action) => {
        alert(`Clicked on ${action} for ${type}`);
        onClose();
    };

    const handleClose = (e) => {
        e.stopPropagation();
        onClose();
    };

    useEffect(() => {
        // Añade un event listener cuando el componente se monta
        document.addEventListener('click', handleClose);
        // Limpia el event listener cuando el componente se desmonta
        return () => {
            document.removeEventListener('click', handleClose);
        };
    }, []);

    return (
        <Menu
            keepMounted
            open={true}
            onClose={onClose}
            anchorReference="anchorPosition"
            anchorPosition={{ top: y, left: x }}
        >
            {type === 'folder' && [

                    <MenuItem onClick={() => handleMenuClick('Open')}>Open</MenuItem>,
                    <MenuItem onClick={() => handleMenuClick('Rename')}>Rename</MenuItem>,
                    <MenuItem onClick={() => handleMenuClick('Delete')}>Delete</MenuItem>

            ]}
            {type === 'file' && [

                    <MenuItem onClick={() => handleMenuClick('View')}>View</MenuItem>,
                    <MenuItem onClick={() => handleMenuClick('Download')}>Download</MenuItem>,
                    <MenuItem onClick={() => handleMenuClick('Delete')}>Delete</MenuItem>

            ]}
        </Menu>
    );
};

export default ContextMenu;
