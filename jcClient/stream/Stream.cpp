#include "Stream.h"
#include "../file/FileManager.h"


void Stream::sendString(const char *c_str) const {
    const int size = static_cast<int>(strlen(c_str));
    sendSize(size);
    if (send(sock, c_str, size, 0) == SOCKET_ERROR) {
        std::cout << "Failed to send message" << std::endl;
    }
}

void Stream::sendSize(int size) const {
    int convertedInt = (int) htonl(size);
    if (send(sock, (char *) &convertedInt, sizeof(size), 0) == SOCKET_ERROR) {
        std::cout << "Failed to send message" << std::endl;
    }


}

std::string Stream::extractRelativePath(const wchar_t *stringPath, const wchar_t *basePath) {
    const std::filesystem::path relativePath = std::filesystem::relative(stringPath,
                                                                         std::filesystem::path(basePath).parent_path());
    return relativePath.string();
}

std::string Stream::extractFileName(const wchar_t *stringPath) {
    const std::filesystem::path tempPath = stringPath;
    return tempPath.filename().string();
}

void Stream::sendFile(const wchar_t *stringPath, const wchar_t *basePath, RESULT &result) const {
    if (FILE *fp = _wfopen(stringPath, L"rb")) {
        fseek(fp, 0L, SEEK_END);
        long int size = ftell(fp);
        fseek(fp, 0L, SEEK_SET);
        std::cout << "size " << size << std::endl;
        sendString(extractRelativePath(stringPath, basePath).c_str());
        std::wcout << extractRelativePath(stringPath, basePath).c_str() << std::endl;
        int download = readSize();
        if (download ==-1) result = RESULT::SR_ERROR;
        else {
            sendSize(size);
            unsigned int readBytes;
            char buffer[16384];
            while ((readBytes = fread(buffer, 1, sizeof(buffer), fp)) > 0) {
                std::cout << readBytes << std::endl;
                if (send(sock, buffer, (int) readBytes, 0) != readBytes) {
                    break;
                }
            }
        }


        fclose(fp);
    } else {
        std::cout << "Incorrect" << std::endl;
    }
}

void Stream::sendFile(const wchar_t *stringPath, RESULT &result) const {
    if (FILE *fp = _wfopen(stringPath, L"rb")) {
        fseek(fp, 0L, SEEK_END);
        long int size = ftell(fp);
        fseek(fp, 0L, SEEK_SET);
        sendString(extractFileName(stringPath).c_str());
        readSize();
        sendSize(size);
        unsigned int readBytes;
        char buffer[16384];
        while ((readBytes = fread(buffer, 1, sizeof(buffer), fp)) > 0) {
            if (send(sock, buffer, (int) readBytes, 0) != readBytes) {
                break;
            }
        }
        fclose(fp);
    } else {
        std::cout << "Incorrect" << std::endl;
        result = RESULT::SR_ERROR;
    }
}

void Stream::sendList(const std::vector<std::string> &list) const {
    const char *const delim = "|";
    std::ostringstream imploded;
    std::copy(list.begin(), list.end(),
              std::ostream_iterator<std::string>(imploded, delim));
    sendString(imploded.str().c_str());
}


std::vector<std::string> Stream::readList() const {
    std::string stringList = readString();
    std::string tmp;
    std::stringstream ss(stringList);
    std::vector<std::string> vectorToDownload;
    while (getline(ss, tmp, '|')) {
        vectorToDownload.push_back(tmp);
    }
    return vectorToDownload;
}


void Stream::readFile(const std::string &destination,RESULT&_result) const {
    sendSize(1); // start reading (sent to Server)
    std::cout << "1st -> Read filename" << std::endl;
    std::filesystem::path fileName = std::filesystem::u8path(readString());
    std::filesystem::path destinationPath = std::filesystem::u8path(destination);
    destinationPath /= fileName;
    std::cout << destinationPath;
    int size = readSize();
    std::cout << "File size -> " << size << std::endl;
    char buffer[16384];
    int total = 0;
    std::ofstream out;
    out.open(destinationPath.c_str(), std::ios::binary | std::ios::out);
    while (total < size) {
        sendSize(0);
        if (readSize()==-1) {
            _result = RESULT::SR_ERROR;
            break;
        }

        const int result = recv(sock, buffer, sizeof(buffer), 0);
        total += result;
        std::cout << total << std::endl;

        out.write(buffer, result);
        if (result == SOCKET_ERROR) {
            std::cout << "Error reading file bytes" << std::endl;
        }

    }

    out.close();
    std::cout << "Sent -> " << total << std::endl;

}


int Stream::readSize(RESULT &_result) const {
    int aux = 0;
    _result = RESULT::SR_UNDEFINED;
    const int result = recv(sock, (char *) &aux, sizeof(int), 0);
    if (result == SOCKET_ERROR) {
        std::cout << "Error reading message size" << std::endl;
        _result = RESULT::SR_ERROR;
        return -1;
    }
    int length = (int) ntohl(aux);
    return length;
}

std::string Stream::readString() const {
    int size = readSize();
    char buffer[size];
    const int result = recv(sock, buffer, size, 0);
    std::string str(buffer, result);
    if (result == SOCKET_ERROR) {
        std::cout << "Error sending message" << std::endl;
    }
    return str;
}


Stream::Stream(SOCKET sock) : sock(sock) {

}

SOCKET Stream::getSock() const {
    return sock;
}
