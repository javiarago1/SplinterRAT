import {useSelector} from "react-redux";

const ProgressBar = () => {
    const progressBars = useSelector(state => state.fileManager.progressBars);
    const progressBarArray = Object.values(progressBars || {});
    const isHidden = progressBarArray.length === 0;

    return (
        <>
            <div className={`downloadPanel ${isHidden ? 'hidden' : ''}`}>
                {progressBarArray.map(download => (
                    <div key={download.channel_id} className="progressItem">
                        <p>
                            {download.progress === 0 ? "Zipping..." : `State: ${download.progress} bytes downloaded`}
                        </p>
                    </div>
                ))}
            </div>
            <style jsx>{`
                .downloadPanel {
                    position: fixed;
                    bottom: 0;
                    left: 0;
                    width: 100%;
                    background-color: rgba(0, 0, 0, 0.7);
                    color: white;
                    padding: 10px;
                    box-sizing: border-box;
                    z-index: 1000;
                    transition: transform 0.3s ease-out, opacity 0.3s ease-out;
                    transform: translateY(0);
                    opacity: 1;
                }

                .hidden {
                    transform: translateY(100%);
                    opacity: 0;
                }

                .progressItem {
                    background-color: rgba(255, 255, 255, 0.1); // Fondo sutil para las barras
                    margin-bottom: 10px; // Espacio entre las barras
                    padding: 5px; 
                    border-radius: 5px; // Esquinas redondeadas
                    transition: opacity 0.5s ease-in-out; // Transición de opacidad para la aparición
                }
            `}</style>
        </>
    );
};

export default ProgressBar;
