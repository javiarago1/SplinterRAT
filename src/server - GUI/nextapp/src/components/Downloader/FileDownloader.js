import {useSelector} from "react-redux";
import {useEffect} from "react";

function FileDownloader() {
    const fileToDownload = useSelector(state => state.client.fileToDownload);

    useEffect(() => {
        if (fileToDownload) {
            console.log("New file to download! "+fileToDownload)
            window.location.href = `${process.env.NEXT_PUBLIC_BACKEND_URL}/${fileToDownload}`;
        }
    }, [fileToDownload]);

    return null;
}


export default FileDownloader;
