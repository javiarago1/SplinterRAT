#include "Stream.h"


void Stream::sendString(const char *c_str) const {
    const int size = (int) strlen(c_str);
    sendSize(size);

    if (send(sock, c_str, size, 0) == SOCKET_ERROR) {
        std::cout << "Failed to send message" << std::endl;
    }
    std::cout << "Message sent" << std::endl;
    readSize();

}

void Stream::sendSize(int size) const {
    int convertedInt = (int) htonl(size);
    if (send(sock, (char *) &convertedInt, sizeof(size), 0) == SOCKET_ERROR) {
        std::cout << "Failed to send message" << std::endl;
    }
    //std::cout << "Size sent" << std::endl;

}

std::string Stream::extractRelativePath(const wchar_t *stringPath, const wchar_t *basePath) {
    const std::filesystem::path relativePath= std::filesystem::relative(stringPath,std::filesystem::path (basePath).parent_path());
    std::cout << "Relative -> " << relativePath << std::endl;
    return relativePath.string();
}

std::string Stream::extractFileName(const wchar_t *stringPath) {
    const std::filesystem::path tempPath = stringPath;
    std::cout << tempPath << std::endl;
    return tempPath.filename().string();
}

void Stream::sendFile(const wchar_t *stringPath,const wchar_t *basePath) const {
    if (FILE *fp = _wfopen(stringPath, L"rb")) {
        fseek(fp, 0L, SEEK_END);
        long int size = ftell(fp);
        fseek(fp, 0L, SEEK_SET);
        std::cout << "size " << size << std::endl;
        sendString(extractRelativePath(stringPath,basePath).c_str());
        readSize();
        sendSize(size);
        unsigned int readBytes;
        char buffer[1024];
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
        std::cout << "size " << size << std::endl;
        sendString(extractFileName(stringPath).c_str());
        readSize();
        sendSize(size);
        unsigned int readBytes;
        char buffer[1024];
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

void Stream::sendList(const std::vector<std::string>& list){
    for (const auto& file: list){
        sendSize(0);
        sendString(file.c_str());
    }
    sendSize(-1);
}


std::vector<std::string> Stream::readList(){
    std::vector<std::string> vectorToDownload;
    while (readSize()!=-1){
        vectorToDownload.push_back(readString());
    }
    return vectorToDownload;
}




void Stream::readFile(const std::vector<std::string>& destinationVector) const {
    sendSize(1); // start reading (sent to Server)
    std::cout << "1st -> Read filename" << std::endl;
    std::wstring wide = Converter::string2wstring(readString());
    std::wcout << wide << std::endl;
    sendSize(69);
    int size = readSize();
    std::cout << "File size -> " << size << std::endl;
    char buffer[4096];
    int total = 0;

    bool firstInteraction=true;
    while (total < size) {
        const int result = recv(sock, buffer, sizeof(buffer), 0);
        total += result;
        std::cout << total << std::endl;

        for (const auto& dest: destinationVector){
            std::wstring finalPath = Converter::string2wstring(dest).append(L"\\"+wide);
            std::ofstream out;
            std::cout << "Exist -> " << std::filesystem::exists(finalPath) << std::endl;
            if (firstInteraction && std::filesystem::exists(finalPath)){
                std::filesystem::resize_file(finalPath,0);
            }
            std::ios_base::openmode mode = std::filesystem::exists(finalPath) ? std::ios::binary | std::ios::app
                    : std::ios::binary |  std::ios::out;
            out.open(finalPath.c_str(), mode);
            out.write(buffer, result);
            out.close();
        }
        firstInteraction=false;

        if (result == SOCKET_ERROR) {
            std::cout << "Error reading file bytes" << std::endl;
        }
    }

    sendSize(-19);
    std::cout << "Sent -> " << total << std::endl;

}


int Stream::readSize() const {
    int aux = 0;
    const int result = recv(sock, (char *) &aux, sizeof(int), 0);
    if (result == SOCKET_ERROR) {
        std::cout << "Error reading message size" << std::endl;
    }
    int length = (int) ntohl(aux);
    //std::cout << "Read -> " << length << std::endl;
    return length;
}

std::string Stream::readString() const {
    int size = readSize();
    std::cout << "Size string -> " << size << std::endl;
    char buffer[1024];
    int total = 0;
    std::string mainString;
    while (total < size) {
        const int result = recv(sock, buffer, sizeof(buffer), 0);
        total += result;
        mainString.append(buffer, result);
        if (result == SOCKET_ERROR) {
            std::cout << "Error sending message" << std::endl;
        }
    }
    sendSize(-69);
    return mainString;

}


Stream::Stream(SOCKET sock) : sock(sock) {

}

SOCKET Stream::getSock() const {
    return sock;
}
