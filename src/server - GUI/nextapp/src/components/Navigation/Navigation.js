import {List, ListItem, Divider, ListItemIcon, ListItemButton, ListItemText} from '@mui/material';
import HomeIcon from '@mui/icons-material/Home';
import ComputerIcon from '@mui/icons-material/Computer';
import InfoIcon from '@mui/icons-material/Info';
import Link from '@mui/material/Link'
import {Factory, PeopleAlt} from "@mui/icons-material";

export default function Navigation() {
    return (
        <List>
            <ListItem disableGutters>
                <Link href="/home" >
                    <ListItemButton>
                        <ListItemIcon>
                            <HomeIcon />
                        </ListItemIcon>
                        <ListItemText primary="Home" />
                    </ListItemButton>
                </Link>
            </ListItem>
            <Divider />
            <ListItem disableGutters>
                <Link href="/" >
                    <ListItemButton>
                        <ListItemIcon>
                            <PeopleAlt />
                        </ListItemIcon>
                        <ListItemText primary="Users" />
                    </ListItemButton>
                </Link>
            </ListItem>
            <ListItem disableGutters>
                <Link href="/builder">
                    <ListItemButton>
                        <ListItemIcon>
                            <Factory />
                        </ListItemIcon>
                        <ListItemText primary="Builder" />
                    </ListItemButton>
                </Link>
            </ListItem>
            <Divider />
            <ListItem disableGutters>
                <Link  href="/about" >
                    <ListItemButton >
                        <ListItemIcon>
                            <InfoIcon />
                        </ListItemIcon>
                        <ListItemText primary="About" />
                    </ListItemButton>
                </Link>
            </ListItem>
        </List>
    );
}
