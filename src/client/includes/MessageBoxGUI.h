#ifndef TESTS_MESSAGEBOXGUI_H
#define TESTS_MESSAGEBOXGUI_H
#include <iostream>
#include <string>
#include "Sender.h"
#include "json.hpp"
#include "ClientSocket.h"
#include "Handler.h"
#include <windows.h>
#include <cstring>
#include <vector>
#include <sstream>
#include <thread>



class MessageBoxGUI : public Handler {
private:
    static UINT getIconFromItem(int);
    std::string boxInformation;
    static UINT getTypeFromItem(int selectedType);
public:
    explicit MessageBoxGUI(ClientSocket &clientSocket);
    void showMessageGUI(nlohmann::json json);


};


#endif
