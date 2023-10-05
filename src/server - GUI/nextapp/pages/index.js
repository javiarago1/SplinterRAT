import React, { useState, useEffect } from 'react';
import ClientManager from '@components/Manager/ClientManager';
import ClientTable from "@components/Table/ClientTable";
import { useDispatch } from 'react-redux';
import { selectClient } from '@redux/slices/clientSlice';
import { WS_CONNECT } from "@redux/actions/connectionActions";

function Home() {
    const [selectedClient, setSelectedClient] = useState(null);
    const dispatch = useDispatch();

    // Connect to the WebSocket when the component mounts
    useEffect(() => {
        dispatch({ type: WS_CONNECT });
    }, [dispatch]);

    function onBack() {
        setSelectedClient(null);
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

export default Home;
