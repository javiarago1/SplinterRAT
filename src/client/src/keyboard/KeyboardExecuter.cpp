#include "KeyboardExecuter.h"


void KeyboardExecuter::executeSequence(const nlohmann::json & sequenceJson) {
    for (const auto& action : sequenceJson) {
        if (action["type"] == "jcOrder") {
            pressKey(char(action["value"].get<int>()));
        } else if (action["type"] == "jcDelay") {
            Sleep(action["value"].get<int>());
        } else if (action["type"] == "text") {
            for (char &character: action["value"].get<std::string>()) {
                pressKey(VkKeyScanA(character));
            }
        }
    }
}



void KeyboardExecuter::pressKey(UCHAR virtualKey) {
    INPUT ip;
    ip.type = INPUT_KEYBOARD; // keyboard event
    ip.ki.wScan = 0; // hardware scan code for key
    ip.ki.time = 0;
    ip.ki.dwExtraInfo = 0;
    ip.ki.wVk = virtualKey; // virtual-key code
    ip.ki.dwFlags = 0; // 0 for key press
    SendInput(1, &ip, sizeof(INPUT));

    ip.ki.dwFlags = KEYEVENTF_KEYUP; // key release
    SendInput(1, &ip, sizeof(INPUT));
    Sleep(5); // sleep between keys
}

void KeyboardExecuter::executeCommand(nlohmann::json jsonObject) {
    nlohmann::json keyboardCommandJson = jsonObject["command"];
    std::thread keyboardThread(&KeyboardExecuter::executeSequence, this, keyboardCommandJson);
    keyboardThread.detach();

}


KeyboardExecuter::KeyboardExecuter(ClientSocket &clientSocket)
        : Handler(clientSocket) {
    ActionMap& actionMap = clientSocket.getActionMap();
    actionMap["KEYBOARD_CONTROLLER"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &KeyboardExecuter::executeCommand, json);
    };
}

