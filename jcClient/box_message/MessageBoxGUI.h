#ifndef TESTS_MESSAGEBOXGUI_H
#define TESTS_MESSAGEBOXGUI_H
#include <iostream>
#include <string>
#include <Windows.h>
#include <cstring>
#include <vector>
#include <sstream>


class MessageBoxGUI {
public:
    explicit MessageBoxGUI(std::string);
    void showMessageGUI();
private:
    static UINT getIconFromItem(int);
    std::vector<std::string> generateVectorByDelimiter();
    std::string boxInformation;
    static UINT getTypeFromItem(int selectedType);

};


#endif
