#ifndef CLIENT_DOWNLOAD_H
#define CLIENT_DOWNLOAD_H


#include "../stream/Stream.h"
#include "../converter/Converter.h"

class Download {
private:
    Stream stream;
    std::vector<std::string> pathVector;
    void downloadFolder(const std::filesystem::path&, const wchar_t *relativePath);
    void downloadFile(const std::filesystem::path&);

public:
    Download(Stream, std::vector<std::string>);
    void start();

};


#endif //CLIENT_DOWNLOAD_H
