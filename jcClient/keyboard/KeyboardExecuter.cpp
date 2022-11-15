

#include "KeyboardExecuter.h"

std::vector<std::string> KeyboardExecuter::getVectorDividedByRegex(const std::string & stringToDivide, const std::regex &regex){
        std::sregex_token_iterator iter(stringToDivide.begin(), stringToDivide.end(), regex, -1);
        std::sregex_token_iterator end;
        return {iter, end};
}

void KeyboardExecuter::executeSequence(){
    std::stringstream f(sequence);
    std::string line;
    std::regex actionRegex("^(jcOrder|jcDelay)\\/(\\d)+$");
    std::regex dividedRegex("/");
    while (std::getline(f, line,'|')) {
        if (std::regex_match(line, actionRegex)){
            std::vector<std::string> dividedVector = getVectorDividedByRegex(line,dividedRegex);
            if (dividedVector[0]=="jcOrder") pressKey(char(stoi(dividedVector[1])));
            else Sleep(stoi(dividedVector[1]));
        } else {
            for(char& character:line) {
                pressKey(VkKeyScanA(character));
            }
        }
    }
}


void KeyboardExecuter::pressKey(UCHAR virtualKey){
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


KeyboardExecuter::KeyboardExecuter(const std::string &sequence) : sequence(sequence) {

}
