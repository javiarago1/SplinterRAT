#ifndef TESTS_MESSAGEBOXGUI_H
#define TESTS_MESSAGEBOXGUI_H
#include <iostream>
#include <string>
#include "../sender/Sender.h"
#include <Windows.h>
#include <cstring>
#include <vector>
#include <sstream>
#include <thread>



class MessageBoxGUI : public Sender {
private:
    static UINT getIconFromItem(int);
    std::string boxInformation;
    std::vector<std::string> generateVectorByDelimiter();
    static UINT getTypeFromItem(int selectedType);
public:
    explicit MessageBoxGUI(const Stream&);
    void showMessageGUI();
    void send() override;


};


#endif
