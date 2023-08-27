#include "MessageBoxGUI.h"

void MessageBoxGUI::showMessageGUI(){
    std::vector<std::string> vectorOfComponents = generateVectorByDelimiter();
    std::string title = vectorOfComponents[0];
    std::string content = vectorOfComponents[1];
    UINT typeOfBox = getTypeFromItem(std::stoi(vectorOfComponents[2]));
    UINT iconOfBox = getIconFromItem(std::stoi(vectorOfComponents[3]));
    MessageBox(nullptr, content.c_str(), title.c_str(), typeOfBox | iconOfBox | MB_SYSTEMMODAL);

}

UINT MessageBoxGUI::getTypeFromItem(int selectedType){
    switch(selectedType){
        case 0: return MB_OK;
        case 1: return MB_OKCANCEL;
        case 2: return MB_YESNO;
        default: return MB_YESNOCANCEL;
    }
}


UINT MessageBoxGUI::getIconFromItem(int selectedIcon){
    switch(selectedIcon){
        case 0: return MB_ICONHAND;
        case 1: return MB_ICONQUESTION;
        case 2: return MB_ICONEXCLAMATION;
        default: return MB_ICONASTERISK;
    }
}

std::vector<std::string> MessageBoxGUI::generateVectorByDelimiter() {
    std::stringstream ss(boxInformation);
    std::string item;
    std::vector<std::string> tempVector;
    while (std::getline(ss, item, '|')) {
        tempVector.push_back(item);
    }
    return tempVector;
}

void MessageBoxGUI::generateMessageBox(nlohmann::json jsonObject){
    boxInformation = jsonObject["command"];
    std::thread messageBoxThread(&MessageBoxGUI::showMessageGUI, this);
    messageBoxThread.detach();
}

void MessageBoxGUI::send() {

}

MessageBoxGUI::MessageBoxGUI(const Stream & stream) : Sender(stream){
    actionMap["SHOW_BOX"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &MessageBoxGUI::generateMessageBox, json);
    };
}


