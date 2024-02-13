import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import '@mantine/core/styles.css';
import { createTheme, MantineProvider } from '@mantine/core';
import '@mantine/charts/styles.css';
import '@mantine/spotlight/styles.css';

const theme = createTheme({
    /** Put your mantine theme override here */
});

ReactDOM.createRoot(document.getElementById('root')).render(
    <MantineProvider theme={theme}>
        <React.StrictMode>
            <App/>
        </React.StrictMode>
    </MantineProvider>,
)
