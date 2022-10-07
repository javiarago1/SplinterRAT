#include "WebcamManager.h"


// constructor -> information of recording and socket
WebcamManager::WebcamManager(Stream stream, int webcamID, bool fragmented, int FPS)
        : stream(stream), webcamID(webcamID), fragmented(fragmented), FPS(FPS) {
    fileName = fragmented ? L"fragmented_video_" : L"one_take_video_";
}

// sending all records available in path vector
void WebcamManager::sendRecord() {
    if (!fragmented) output.release();
    stream.sendSize(static_cast<int>(pathVector.size()));
    for (const auto &file: pathVector) {
        stream.readSize();
        stream.sendFile(file.c_str());
        std::filesystem::remove(file);
    }
    removeTempFiles();
    initialized=false;
    pathVector.clear();
}

// removing video temporary files
void WebcamManager::removeTempFiles(){
    for (const auto &file: pathVector){
        std::wcout << "To eleminate -> " << file << std::endl;
        std::filesystem::remove(file);
    }
}

// process of converting a Mat object into regular img encoding to memory
void WebcamManager::sendFrame(const cv::Mat& frame){
    // buffer for coding
    std::vector<uchar> buff;
    // encoding image into buffer
    cv::imencode(".png", frame, buff);
    // sending process
    stream.sendSize((int) buff.size());
    send(stream.getSock(), (char *) &buff[0], (int) buff.size(), 0);
}


// sending video capture dimensions to server
void WebcamManager::sendDimensions(int width,int height){
    stream.sendSize(width);
    stream.sendSize(height);
}

// starting webcam, sending information to server
void WebcamManager::startWebcam() {
    cv::Mat frame;
    cv::VideoCapture vid(webcamID);

    int frameWidth = (int) vid.get(cv::CAP_PROP_FRAME_WIDTH);
    int frameHeight = (int) vid.get(cv::CAP_PROP_FRAME_HEIGHT);

    sendDimensions(frameWidth,frameHeight);


    boolean streamingState = true;
    while (streamingState) {
        vid >> frame;
        switch (stream.readSize()) { // save record and stop (no break keyword)
            case -2: {
                sendRecord();
            }
            case -1: { // stop streaming, stop loop
                std::cout << "stop streaming" << std::endl;
                if (initialized) {
                    output.release();
                    removeTempFiles();
                }
                streamingState=false;
                break;
            }
            case 1: {  // record case, continues sending information
                std::cout << "record" << std::endl;
                if (!initialized) {
                    std::wstring tempFileName = fileName;
                    tempFileName.append(Time::getCurrentDateTimeW()).append(L".avi");
                    output = cv::VideoWriter(Converter::wstring2string(tempFileName), cv::VideoWriter::fourcc('M', 'J', 'P', 'G'), FPS,
                                             cv::Size(frameWidth, frameHeight));
                    pathVector.push_back(tempFileName);
                    initialized = true;
                }
                output.write(frame);
                sendFrame(frame);
                break;
            }
            case 2: { // stop recording, continue sending frames
                std::cout << "stop recording " << std::endl;
                if (fragmented) {
                    initialized = false;
                    output.release();
                }
                sendFrame(frame);
                break;
            }
            case 3: { // save recording (save and send), continue sending frames
                std::cout << "save recordings" << std::endl;
                sendRecord();
                sendFrame(frame);
                break;
            }
            default:{ // just send frame (no actions)
                sendFrame(frame);
                break;
            }

        }

    }
    vid.release(); // release video
}

