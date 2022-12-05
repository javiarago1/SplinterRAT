#include "Download.h"

Download::Download(const Stream &stream) : Sender(stream) {}

void Download::start() {
    std::vector<std::string> fileList = stream.readList();
    for (const auto &file: fileList) {
        std::wstring wide = Converter::string2wstring(file);
        if (std::filesystem::is_directory(wide)) {
            downloadFolder(wide, wide.c_str());
        } else {
            downloadFile(wide);
        }
    }
    stream.sendString("/");
}

void Download::downloadFolder(const std::filesystem::path &filePath, const wchar_t *relativePath) {
    for (const auto &entry: std::filesystem::directory_iterator(filePath)) {
        try {
            if (entry.is_directory()) {
                downloadFolder(entry, relativePath);
            } else {
                stream.sendFile(entry.path().wstring().c_str(), relativePath);
            }
        } catch (std::filesystem::__cxx11::filesystem_error&) {}
    }
}

void Download::downloadFile(const std::filesystem::path &filePath) {
    stream.sendFile(filePath.wstring().c_str());
}

void Download::send() {

}



