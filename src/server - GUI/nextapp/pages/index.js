import React, { useState, useEffect } from 'react';
import ClientManager from '@components/Manager/ClientManager';
import ClientTable from "@components/Table/ClientTable";
import {useDispatch, useSelector} from 'react-redux';
import { removeSelectedClient, selectClient} from '@redux/slices/clientSlice';
import { SELECT_CLIENT, WS_CONNECT} from "@redux/actions/connectionActions";

function Home() {
    const dispatch = useDispatch();
    const selectedClient = useSelector(state => state.client.selectedClient);


    useEffect(() => {
        dispatch({ type: WS_CONNECT });
    }, [dispatch]);

    const onClientSelect =  (client) => {
        dispatch({type: SELECT_CLIENT, payload: { client_id: client.systemInformation.UUID, set_null: false} });
    };

    const onBack = () => {
        dispatch({type: SELECT_CLIENT, payload: { client_id: selectedClient.systemInformation.UUID, set_null: true} })
        dispatch(removeSelectedClient())
        console.log("Selected client after going back: " +selectClient)
    }


    return (
        <div  style={{height: "100%"}}>
            {!selectedClient ?
                <ClientTable onClientSelect={onClientSelect} /> :
                !selectClient ?
                    <p>Loading...</p> :
                    <ClientManager client={selectedClient} onBack={onBack} />
            }
        </div>
    );
}

export default Home;
