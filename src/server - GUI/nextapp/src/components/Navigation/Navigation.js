import Link from 'next/link';
import { Button, Drawer, List, ListItem } from '@mui/material';

export default function Navigation() {
    return (
        <List>
            <ListItem>
                <Link href="/" passHref>
                    <Button fullWidth>Inicio</Button>
                </Link>
            </ListItem>
            <ListItem>
                <Link href="/clients" passHref>
                    <Button fullWidth>Clientes</Button>
                </Link>
            </ListItem>
            <ListItem>
                <Link href="/compiler" passHref>
                    <Button fullWidth>Compilador</Button>
                </Link>
            </ListItem>
        </List>
    );
}
