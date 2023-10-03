import React, { useState, useEffect } from 'react';
import ClientManager from './ClientManager';
import ClientTable from "./ClientTable";
import { useDispatch } from 'react-redux';
import { selectClient } from './clientSlice';
import { WS_CONNECT} from "./actionTypes";

function App() {
    const [selectedClient, setSelectedClient] = useState(null);
    const dispatch = useDispatch();

    // Connect to the WebSocket when the component mounts
    useEffect(() => {
        dispatch({ type: WS_CONNECT });
    }, [dispatch]);

    function onBack(){
        setSelectedClient(null)
    }


    return (
        <div>
            {!selectedClient ?
                <ClientTable onClientSelect={(client) => {
                    dispatch(selectClient(client));
                    setSelectedClient(client);
                }} /> :
                <ClientManager client={selectedClient} onBack={onBack} />
            }
        </div>
    );
}

export default App;
