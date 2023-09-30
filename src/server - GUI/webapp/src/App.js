import React, { useState, useEffect } from 'react';
import { useDispatch } from 'react-redux'; // importa useDispatch
import { setClients } from './clientSlice'; // importa setClients
import { Provider } from 'react-redux';
import { store } from './store';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import MainTabs from './MainTabs';

function App() {
    const [ws, setWs] = useState(null);
    const dispatch = useDispatch(); // define dispatch usando el hook useDispatch

    useEffect(() => {
        const websocket = new WebSocket('ws://127.0.0.1:3055/web');

        websocket.onopen = () => {
            console.log("Conexión establecida.");
        };

        websocket.onmessage = (event) => {
            const data = JSON.parse(event.data);

            if (data.RESPONSE === "TABLE_INFO") {
                dispatch(setClients(data.content));
            }

            console.log("Mensaje recibido:", event.data);
        };

        websocket.onerror = (error) => {
            console.error("Error en WebSocket:", error);
        };

        websocket.onclose = (event) => {
            console.log("Conexión cerrada. Código:", event.code, "Razón:", event.reason);
        };

        setWs(websocket);

        return () => {
            websocket.close();
        };
    }, [dispatch]); // agrega dispatch como dependencia al useEffect

    return (
        <Router>
            <div>
                <nav>
                    <ul>
                        <li>
                            <Link to="/">Home</Link>
                        </li>
                    </ul>
                </nav>

                <Routes>
                    <Route path="/" element={<MainTabs />} />
                </Routes>
            </div>
        </Router>
    );
}

function AppWrapper() {
    return (
        <Provider store={store}>
            <App />
        </Provider>
    );
}

export default AppWrapper;
