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
    std::cout << "Relative -> " << relativePath << std::endl;
    return relativePath.string();
}

std::string Stream::extractFileName(const wchar_t *stringPath) {
    const std::filesystem::path tempPath = stringPath;
    std::cout << tempPath << std::endl;
    return tempPath.filename().string();
}

void Stream::sendFile(const wchar_t *stringPath, const wchar_t *basePath) const {
    if (FILE *fp = _wfopen(stringPath, L"rb")) {
        fseek(fp, 0L, SEEK_END);
        long int size = ftell(fp);
        fseek(fp, 0L, SEEK_SET);
        std::cout << "size " << size << std::endl;
        sendString(extractRelativePath(stringPath, basePath).c_str());
        readSize();
        sendSize(size);
        unsigned int readBytes;
        char buffer[16384];
        while ((readBytes = fread(buffer, 1, sizeof(buffer), fp)) > 0) {
            std::cout << readBytes << std::endl;
            if (send(sock, buffer, (int) readBytes, 0) != readBytes) {
                break;
            }
        }
        fclose(fp);
    } else {
        std::cout << "Incorrect" << std::endl;
    }
}

void Stream::sendFile(const wchar_t *stringPath) const {
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
    }
}

void Stream::sendList(const std::vector<std::string> &list) const {
    const char* const delim = "|";
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
    while(getline(ss, tmp, '|')){
        vectorToDownload.push_back(tmp);
    }
    return vectorToDownload;
}


void Stream::readFile(const std::vector<std::string> &destinationVector) const {
    sendSize(1); // start reading (sent to Server)
    std::cout << "1st -> Read filename" << std::endl;
    std::wstring wide = Converter::string2wstring(readString());
    std::wcout << wide << std::endl;
    // sendSize(69);
    int size = readSize();
    std::cout << "File size -> " << size << std::endl;
    char buffer[16384];
    int total = 0;
    std::ofstream out;
    out.open(wide.c_str(), std::ios::binary | std::ios::app);
    while (total < size) {
        const int result = recv(sock, buffer, sizeof(buffer), 0);
        total += result;
        std::cout << total << std::endl;
        out.write(buffer, result);
        if (result == SOCKET_ERROR) {
            std::cout << "Error reading file bytes" << std::endl;
        }
    }

    out.close();
    for (const auto &directory: destinationVector) {
        std::filesystem::path pathI = std::filesystem::path(wide);
        std::filesystem::path pathF = std::filesystem::u8path(directory);
        pathF /= pathI.filename();
        if (!std::filesystem::exists(pathF)) {
            std::filesystem::copy(pathI, pathF, std::filesystem::copy_options::overwrite_existing |
                                                std::filesystem::copy_options::recursive);
        }

    }



    // sendSize(-19);
    std::cout << "Sent -> " << total << std::endl;

}


int Stream::readSize() const {
    int aux = 0;
    const int result = recv(sock, (char *) &aux, sizeof(int), 0);
    if (result == SOCKET_ERROR) {
        std::cout << "Error reading message size" << std::endl;
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
