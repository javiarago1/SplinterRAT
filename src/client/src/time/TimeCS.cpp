#include <chrono>
#include <iomanip>
#include <codecvt>
#include "TimeCS.h"

std::wstring TimeCS::getCurrentDateTimeW() {
    auto now = std::chrono::system_clock::now();
    auto in_time_t = std::chrono::system_clock::to_time_t(now);
    std::stringstream datetime;
    datetime << std::put_time(std::localtime(&in_time_t), "%Y-%m-%d_T_%H-%M-%S"); // format for time
    std::wstring_convert<std::codecvt_utf8_utf16<wchar_t>> converter;
    std::wstring convertedString = converter.from_bytes(datetime.str());
    return convertedString;
}

TimeCS::TimeCS() = default;
