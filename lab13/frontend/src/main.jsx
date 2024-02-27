import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import '@mantine/core/styles.css';
import { createTheme, MantineProvider } from '@mantine/core';
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import '@mantine/charts/styles.css';
import '@mantine/spotlight/styles.css';
import {routes} from "./routes.jsx";

const theme = createTheme({
    /** Put your mantine theme override here */
});

const router = createBrowserRouter(routes)

ReactDOM.createRoot(document.getElementById('root')).render(
    <MantineProvider theme={theme}>
        <React.StrictMode>
            <RouterProvider router={router}/>
        </React.StrictMode>
    </MantineProvider>,
)
