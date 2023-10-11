import Link from 'next/link';
import {List, ListItem, Divider, ListItemIcon, Typography, ListItemButton, ListItemText} from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import ComputerIcon from '@mui/icons-material/Computer';
import UsersIcon from '@mui/icons-material/People';
import {Info} from "@mui/icons-material";


export default function Navigation() {
    return (
        <List>
            <ListItem>
                <ListItemButton href="/home">
                    <ListItemIcon>
                        <HomeIcon/>
                    </ListItemIcon>
                    <ListItemText primary="Home"/>
                </ListItemButton>
            </ListItem>
            <Divider/>
            <ListItem>
                <ListItemButton href="/">
                    <ListItemIcon>
                        <UsersIcon/>
                    </ListItemIcon>
                    <ListItemText primary="Users"/>
                </ListItemButton>
            </ListItem>
            <ListItem>
                <ListItemButton href="/compiler">
                    <ListItemIcon>
                        <ComputerIcon/>
                    </ListItemIcon>
                    <ListItemText primary="Compiler"/>
                </ListItemButton>
            </ListItem>
            <Divider/>
            <ListItem>
                <ListItemButton href="/about">
                    <ListItemIcon>
                        <Info/>
                    </ListItemIcon>
                    <ListItemText primary="About"/>
                </ListItemButton>
            </ListItem>
        </List>
    );
}
