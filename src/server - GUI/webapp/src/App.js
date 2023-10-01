// App.js

import React, { useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { Provider } from 'react-redux';
import { store } from './store';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import MainTabs from './MainTabs';

function App() {
    const dispatch = useDispatch();

    useEffect(() => {
        dispatch({ type: 'WS_CONNECT' });

        return () => {
            dispatch({ type: 'WS_DISCONNECT' });
        };
    }, [dispatch]);

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
