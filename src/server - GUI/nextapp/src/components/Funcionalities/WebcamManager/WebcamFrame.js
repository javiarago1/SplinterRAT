import React from 'react';
import { useSelector } from 'react-redux';

const WebcamFrame = () => {
    const frame = useSelector(state => state.webcamManager.frame);

    return (
        <div style={{ marginTop: '20px', marginBottom: '20px' }}>
            {frame && (
                <img
                    src={frame}
                    alt="Webcam"
                    style={{ width: '100%', maxWidth: '640px', border: '1px solid #ccc' }}
                />
            )}
        </div>
    );
};

export default WebcamFrame;
