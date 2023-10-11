import React from 'react';
import { useSelector } from 'react-redux';
import styles from './WebcamFrame.module.css';

const WebcamFrame = () => {
    const {frame, isWebcamOn }= useSelector(state => state.webcamManager);

    return (
        <div className={styles.webcamFrame}>
            {isWebcamOn && frame ? (
                <img
                    src={frame}
                    alt="Webcam"
                    className={styles.webcamImage}
                />
            ) : (<>
                <p className={styles.placeholderText}>Select a camara and start it!</p>
                </>
            )}
        </div>
    );
};

export default WebcamFrame;
