
#ifndef CLIENT_CONVERTER_H
#define CLIENT_CONVERTER_H

#include <string>
#include <locale>
#include <codecvt>

class Converter {
public:
    Converter();
    static std::wstring string2wstring(const std::string&);
    static std::string wstring2string(const std::wstring&);
};


#endif //CLIENT_CONVERTER_H
