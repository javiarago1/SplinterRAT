#ifndef TESTS_MESSAGEBOXGUI_H
#define TESTS_MESSAGEBOXGUI_H
#include <iostream>
#include <string>
#include <Windows.h>
#include <cstring>
#include <vector>
#include <sstream>
#include <thread>
#include "../sender/Sender.h"


class MessageBoxGUI : public Sender {
private:
    static UINT getIconFromItem(int);
    static std::vector<std::string> generateVectorByDelimiter(const std::string&);
    static UINT getTypeFromItem(int selectedType);
public:
    explicit MessageBoxGUI(const Stream&);
    void showMessageGUI();
    void send() override;


};


#endif
