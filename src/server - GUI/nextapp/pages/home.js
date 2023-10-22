import React from 'react';
import {Box, Container, Divider, Paper, Typography} from '@mui/material';
import Link from "@mui/material/Link";

const HomePage = () => {
    return (
        <Paper sx={{maxHeight: '85vh', height: '85vh'}} elevation={10}>
            <Container maxWidth="md" sx={{ ml:0, p:1 }}>
                <Box sx={{p: 3}}>
                    <Typography variant="h4" gutterBottom>
                        The Philosophy Behind RATs
                    </Typography>

                    <Typography variant="subtitle2" gutterBottom>
                        <i>{"\"Just because you can doesn't mean you should.\""}</i> - <strong><Link href="https://en.wikipedia.org/wiki/Kevin_Mitnick" target="_blank" rel="noopener noreferrer">Kevin Mitnick</Link></strong>
                    </Typography>

                    <Divider sx={{mt: 3, mb: 3}}/>

                    <Typography paragraph>
                        The history of Remote Administration Tools (RATs) is a captivating tale of ingenuity and innovation. Since their inception, these tools have facilitated tasks ranging from legitimate remote system management to more clandestine operations. They have been the unseen hand orchestrating a vast symphony of interconnected devices, playing both the savior in IT management and the villain in cyber espionage tales.
                    </Typography>

                    <Typography paragraph>
                        Developing SplinterRAT was a deep dive into this rich tapestry of technological advancement. It became more than just coding, it was a journey into understanding the profound impact such tools could have on individuals and systems. As the lines of code multiplied and its capabilities expanded, an ethical dilemma began to unfold. The power to control, manage, and manipulate systems remotely comes with an immense responsibility.
                    </Typography>

                    <Typography paragraph>
                        Throughout the development, my initial excitement was tempered by contemplation. The more I understood its potential, the more I found myself reflecting on its rightful place in the world. Instead of making it publicly available, I began to see SplinterRAT as a deeply personal project, born from curiosity but bounded by ethical considerations. The choice to keep it as a testament to my exploration into the world of RATs, rather than a tool for public use, was fueled by the moral responsibility that accompanies such power. In the ever-evolving world of technology, it is essential to remember that just because we can, doesn&apos;t always mean we should.
                    </Typography>


                </Box>
            </Container>
        </Paper>
    );
};

export default HomePage;
