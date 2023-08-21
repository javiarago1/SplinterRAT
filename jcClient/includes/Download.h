#ifndef CLIENT_DOWNLOAD_H
#define CLIENT_DOWNLOAD_H


#include <thread>
#include "Stream.h"
#include "Converter.h"
#include "Sender.h"
#include "json.hpp"


class Download : public Sender {

private:
    bool download=true;
    void downloadFolder(const std::filesystem::path &, const wchar_t *relativePath);
    void downloadFile(const std::wstring &filePath,const std::wstring &basePath);

public:
    explicit Download(const Stream&);
    void downloadContent(nlohmann::json jsonObject);
    void send() override;
    void uploadFiles(nlohmann::json jsonObject);
};


#endif
