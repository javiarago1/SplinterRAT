import React, { useRef, useEffect } from 'react';
import { useSelector } from 'react-redux';
import styles from './WebcamFrame.module.css';

const WebcamFrame = () => {
    const { frame, isWebcamOn } = useSelector(state => state.webcamManager);
    const canvasRef = useRef(null);

    useEffect(() => {
        if (isWebcamOn && frame) {
            const canvas = canvasRef.current;
            const ctx = canvas.getContext('2d');

            const img = new Image();
            img.src = frame;
            img.onload = () => {
                // Ajustar el tamaño del canvas al de la imagen.
                canvas.width = img.width;
                canvas.height = img.height;
                ctx.drawImage(img, 0, 0, img.width, img.height);
            };
        }
    }, [frame, isWebcamOn]);

    return (
        <div className={styles.webcamFrame}>
            {isWebcamOn && frame ? (
                <canvas ref={canvasRef}  className={styles.webcamImage} />
            ) : (
                <p className={styles.placeholderText}>Select a camera and start it!</p>
            )}
        </div>
    );
};

export default WebcamFrame;
