#ifndef CLIENT_DOWNLOAD_H
#define CLIENT_DOWNLOAD_H


#include "../stream/Stream.h"
#include "../converter/Converter.h"
#include "../Sender/Sender.h"

class Download : public Sender {

private:
    bool download=true;
    void downloadFolder(const std::filesystem::path &, const wchar_t *relativePath);
    void downloadFile(const std::wstring &filePath,const std::wstring &basePath);

public:
    explicit Download(const Stream&);
    void start();
    void send() override;

};


#endif //CLIENT_DOWNLOAD_H
