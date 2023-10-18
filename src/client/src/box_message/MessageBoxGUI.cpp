#include "MessageBoxGUI.h"


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


void MessageBoxGUI::showMessageGUI(nlohmann::json json) {
    std::string title = json["title"];
    std::string content = json["content"];
    UINT typeOfBox = getTypeFromItem(json["type"]);
    UINT iconOfBox = getIconFromItem(json["icon"]);
    MessageBox(nullptr, content.c_str(), title.c_str(), typeOfBox | iconOfBox | MB_SYSTEMMODAL);
}

MessageBoxGUI::MessageBoxGUI(ClientSocket &clientSocket)
        : Handler(clientSocket){
    ActionMap& actionMap = clientSocket.getActionMap();
    actionMap["SHOW_MESSAGE_BOX"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &MessageBoxGUI::showMessageGUI, json["info"]);
    };
}


