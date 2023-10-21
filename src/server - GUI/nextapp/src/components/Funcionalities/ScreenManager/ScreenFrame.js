import React, {useRef, useEffect, useCallback} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import styles from './ScreenFrame.module.css';
import {KEY_EXECUTION} from "@redux/actions/screenManagerActions";

const ScreenFrame = () => {
    const dispatch = useDispatch();
    const { frame, isScreenOn, originalDimensions, canControl } = useSelector(state => state.screenManager);
    const canvasRef = useRef(null);

    const typeOfClick = (e) => {
        let clickType = {
            type: "click",
            values: []
        };

        switch (e.button) {
            case 0:
                clickType.values = [2, 4];
                break;
            case 2:
                clickType.values = [8, 16];
                break;
            default:
                break;
        }

        return clickType;
    };

    const handleKeyDown = useCallback((e) => {
        if (!canControl) return;

        if (e.key.length === 1){
            const charCode = e.key.charCodeAt(0);
            dispatch({type: KEY_EXECUTION, payload:{keyEvent: charCode} })

        }
    }, [dispatch]);

    useEffect(() => {
        window.addEventListener('keydown', handleKeyDown);
        return () => {
            window.removeEventListener('keydown', handleKeyDown);
        };
    }, [handleKeyDown]);

    const handleClick = useCallback((e) => {
        if (!canControl) return;

        const canvas = canvasRef.current;
        if (!canvas) return;

        const rect = canvas.getBoundingClientRect();
        const elementRelativeX = e.clientX - rect.left;
        const elementRelativeY = e.clientY - rect.top;
        const canvasRelativeX = elementRelativeX * canvas.width / rect.width;
        const canvasRelativeY = elementRelativeY * canvas.height / rect.height;
        const clickInfo = typeOfClick(e);

        const clickEvent = {
            x: canvasRelativeX,
            y: canvasRelativeY,
            clickType: clickInfo.type,
            values: clickInfo.values
        };

        console.log(JSON.stringify(clickEvent));
        dispatch({type: KEY_EXECUTION, payload: clickEvent});
    }, [canControl, dispatch]);  // <-- arreglo de dependencias vacío para useCallback

    useEffect(() => {
        if (isScreenOn && frame) {
            const screenCanvas = canvasRef.current;
            const ctx = screenCanvas.getContext('2d');

            const img = new Image();
            img.src = frame;
            img.onload = () => {
                screenCanvas.width = img.width;
                screenCanvas.height = img.height;
                ctx.drawImage(img, 0, 0, img.width, img.height);
            };
        }
    }, [frame, canControl, isScreenOn]);

    useEffect(() => {
        const canvas = canvasRef.current;
        if (canvas) {
            const preventDefaultRightClick = (e) => e.preventDefault();

            canvas.addEventListener('contextmenu', preventDefaultRightClick);
            canvas.addEventListener('mousedown', handleClick);

            return () => {
                canvas.removeEventListener('contextmenu', preventDefaultRightClick);
                canvas.removeEventListener('mousedown', handleClick);
            };
        }
    }, [handleClick, canControl, frame, isScreenOn]);

    return (
        <div className={styles.screenFrame}>
            {isScreenOn && frame ? (
                <canvas ref={canvasRef}  className={styles.screenImage} />
            ) : (
                <p className={styles.placeholderText}>Select a screen and start it!</p>
            )}
        </div>
    );
};

export default ScreenFrame;
