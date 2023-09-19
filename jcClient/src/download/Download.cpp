#include "Download.h"
#include <fstream>
#include <vector>
#include <algorithm>

Download::Download(ClientSocket &clientSocket)
: Handler(clientSocket) {
    ActionMap& actionMap = clientSocket.getActionMap();
    actionMap["DOWNLOAD"]  = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &Download::downloadContent, json);
    };
    actionMap["UPLOAD"] = [&](nlohmann::json& json) {
     //   threadGen.runInNewThread(this, &Download::uploadFiles, json);
    };
}



const int FRAGMENT_SIZE = 64 * 1024; // 64 KB
const uint8_t LAST_FRAGMENT = 0x02;
const uint8_t NOT_LAST_FRAGMENT = 0x01;

void Download::downloadContent(nlohmann::json jsonObject) {
    std::string outputPath = ZipCompressor::compressPath(jsonObject["from_path"]);
    byte fileID = jsonObject["channel_id"];
    std::cout << "Output path: " + outputPath << std::endl;

    std::ifstream fileStream(outputPath, std::ios::in | std::ios::binary);

    if (!fileStream) {
        std::cout << "Could not open file for reading: " << outputPath << std::endl;
        return;
    }

    std::vector<uint8_t> buffer(FRAGMENT_SIZE);

    // Preparing header bytes: 1 byte for file ID, 1 byte for control code
    buffer[0] = fileID;
    buffer[1] = NOT_LAST_FRAGMENT;

    while (fileStream) {
        // Read the file chunk into the buffer, starting at offset 2 to leave space for control bytes
        fileStream.read((char*)buffer.data() + 2, FRAGMENT_SIZE - 2);
        std::streamsize bytesRead = fileStream.gcount();

        if (bytesRead == 0) {
            break;
        }

        if (!fileStream) {
            // Last fragment
            buffer[1] = LAST_FRAGMENT;
        }

        // Send the fragment
        // Note: Sending bytesRead + 2 bytes to include the 2 control bytes
        clientSocket.sendBytes(std::vector<uint8_t>(buffer.begin(), buffer.begin() + bytesRead + 2));
    }

    fileStream.close();
}

void Download::downloadContentFromVector(const std::vector<uint8_t>& content, byte fileID) {
    std::vector<uint8_t> buffer(FRAGMENT_SIZE);

    // Preparing header bytes: 1 byte for file ID, 1 byte for control code
    buffer[0] = fileID;
    buffer[1] = NOT_LAST_FRAGMENT;

    size_t contentSize = content.size();
    size_t offset = 0;

    while (offset < contentSize) {
        // Calculate how many bytes we will send in this fragment
        size_t remainingBytes = contentSize - offset;
        size_t bytesToSend;
        if (remainingBytes < (FRAGMENT_SIZE - 2)) {
            bytesToSend = remainingBytes;
        } else {
            bytesToSend = FRAGMENT_SIZE - 2;
        }

        // Copy the content to the buffer, starting at offset 2 to leave space for control bytes
        std::copy(content.begin() + offset, content.begin() + offset + bytesToSend, buffer.begin() + 2);

        // Check if this is the last fragment
        if (offset + bytesToSend >= contentSize) {
            buffer[1] = LAST_FRAGMENT;
        }

        // Send the fragment
        // Note: Sending bytesToSend + 2 bytes to include the 2 control bytes
        clientSocket.sendBytes(std::vector<uint8_t>(buffer.begin(), buffer.begin() + bytesToSend + 2));

        // Update the offset
        offset += bytesToSend;
    }
}




/*

void Download::uploadFiles(nlohmann::json jsonObject){
    std::string destinationPath = jsonObject["path"];
    int numOfFiles = jsonObject["num_of_files"];
    RESULT result;
    for (int i = 0; i < numOfFiles; i++) {
        stream.readFile(destinationPath,result);
        if (result==RESULT::SR_ERROR) i=numOfFiles;
    }
}


void Download::downloadContent(nlohmann::json jsonObject) {
    std::vector<std::string> fileList = jsonObject["file_list"];
    for (const auto &file: fileList) {
        if(!download) break;
        std::wstring wide = Converter::string2wstring(file);
        if (std::filesystem::is_directory(wide)) {
            downloadFolder(wide, wide.c_str());
        } else {
            downloadFile(wide,wide);
        }
    }
    stream.sendString("/");
}

void Download::downloadFolder(const std::filesystem::path &filePath, const wchar_t *relativePath) {
    for (const auto &entry: std::filesystem::directory_iterator(filePath)) {
        if (!download) break;
        try {
            if (entry.is_directory()) {
                downloadFolder(entry, relativePath);
            } else {
                downloadFile(entry.path().wstring(),relativePath);
            }
        } catch (std::filesystem::__cxx11::filesystem_error&) {}
    }
}

void Download::downloadFile(const std::wstring &filePath,const std::wstring &basePath) {
    RESULT result;
    stream.sendFile(filePath.c_str(),basePath.c_str(),result);
    if (result==RESULT::SR_ERROR) download=false;
}

void Download::send() {


 }
*/




