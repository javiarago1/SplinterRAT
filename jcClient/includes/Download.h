#ifndef CLIENT_DOWNLOAD_H
#define CLIENT_DOWNLOAD_H


#include <thread>
#include "Stream.h"
#include "Converter.h"
#include "Sender.h"
#include "json.hpp"
#include "ZipCompressor.h"
#include "Handler.h"


class Download : public Handler {

private:
    std::atomic<bool>download = true;
    //void downloadFolder(const std::filesystem::path &, const wchar_t *relativePath);
    //void downloadFile(const std::wstring &filePath,const std::wstring &basePath);
public:
    explicit Download(ClientSocket &clientSocket);
    void downloadContent(nlohmann::json jsonObject);
    void downloadContentFromVector(const std::vector<uint8_t>& content, byte fileID);
    //void send() override;
    //void uploadFiles(nlohmann::json jsonObject);
};


#endif
