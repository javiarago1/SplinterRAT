#include "Converter.h"

std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter;

Converter::Converter() {

}

std::wstring Converter::string2wstring(const std::string& string) {
    return converter.from_bytes(string);
}

std::string Converter::wstring2string(const std::wstring& string) {
    return converter.to_bytes(string);
}


